const fs = require('fs');
const path = require('path');

function scanComponents(dir, baseDir = dir, depth = 0, maxDepth = 10) {
    if (depth > maxDepth) {
        console.log(`达到最大深度限制: ${maxDepth}, 停止扫描: ${dir}`);
        return [];
    }

    // 检查目录是否存在
    if (!fs.existsSync(dir)) {
        console.warn(`目录不存在: ${dir}`);
        return [];
    }

    try {
        const items = fs.readdirSync(dir);
        const components = [];

        console.log(`扫描目录: ${dir} (深度: ${depth})`);

        for (const item of items) {
            // 跳过隐藏文件和特定目录
            if (item.startsWith('.') || item === 'node_modules' || item === 'dist' || item === 'build') {
                continue;
            }

            const fullPath = path.join(dir, item);

            try {
                const stat = fs.statSync(fullPath);

                if (stat.isDirectory()) {
                    // 递归扫描子目录
                    const subComponents = scanComponents(fullPath, baseDir, depth + 1, maxDepth);
                    components.push(...subComponents);
                } else if (stat.isFile() && (item.endsWith('.tsx') || item.endsWith('.jsx'))) {
                    // 跳过测试文件
                    if (item.includes('.test.') || item.includes('.spec.')) {
                        continue;
                    }

                    console.log(`分析文件: ${fullPath}`);
                    const content = fs.readFileSync(fullPath, 'utf-8');

                    // 检查是否是React组件
                    const hasReactImport = /import.*from\s+['"]react['"]/i.test(content) ||
                        /import\s+React/i.test(content);
                    const hasJSX = /<[A-Z]/.test(content) ||
                        /React\.(createElement|Component|Fragment)/.test(content);

                    const isReactComponent = hasReactImport && hasJSX;

                    if (isReactComponent) {
                        const relativePath = path.relative(baseDir, fullPath).replace(/\\/g, '/');
                        const componentInfo = {
                            name: item.replace(/\.(tsx|jsx)$/, ''),
                            filePath: relativePath,
                            hasDefaultExport: content.includes('export default'),
                            exports: extractExports(content),
                            hasReactImport: hasReactImport,
                            hasJSX: hasJSX
                        };

                        components.push(componentInfo);
                        console.log(`发现React组件: ${componentInfo.name}`);
                    }
                }
            } catch (err) {
                console.warn(`无法访问: ${fullPath}`, err.message);
            }
        }

        return components;
    } catch (err) {
        console.error(`扫描目录错误: ${dir}`, err.message);
        return [];
    }
}

function extractExports(content) {
    const exports = [];

    // 匹配命名导出
    const patterns = [
        /export\s+const\s+(\w+)/g,
        /export\s+function\s+(\w+)/g,
        /export\s+class\s+(\w+)/g,
        /export\s+interface\s+(\w+)/g,
        /export\s+type\s+(\w+)/g,
        /export\s+{\s*([^}]+)\s*}/g
    ];

    patterns.forEach(pattern => {
        let match;
        while ((match = pattern.exec(content)) !== null) {
            if (match[1]) {
                // 处理 export { A, B, C } 格式
                if (match[1].includes(',')) {
                    match[1].split(',').forEach(exp => {
                        const trimmed = exp.trim();
                        if (trimmed && !exports.includes(trimmed)) {
                            exports.push(trimmed);
                        }
                    });
                } else if (!exports.includes(match[1])) {
                    exports.push(match[1]);
                }
            }
        }
    });

    return exports;
}

// 主函数
function main() {
    try {
        console.log('开始扫描React组件...');

        // 获取项目根目录（脚本所在目录的上级的上级）
        const scriptDir = __dirname;
        const projectRoot = path.resolve(scriptDir, '../..'); // 回到项目根目录

        console.log(`脚本目录: ${scriptDir}`);
        console.log(`项目根目录: ${projectRoot}`);

        // 尝试不同的src目录路径
        const possibleSrcDirs = [
            path.join(projectRoot, 'src'),
            path.join(projectRoot, 'flash-react-app', 'src'),
            projectRoot // 直接扫描项目根目录
        ];

        let srcDir = null;
        for (const dir of possibleSrcDirs) {
            if (fs.existsSync(dir)) {
                srcDir = dir;
                console.log(`找到源目录: ${srcDir}`);
                break;
            }
        }

        if (!srcDir) {
            console.log('未找到src目录，尝试扫描项目根目录...');
            srcDir = projectRoot;
        }

        // 确保public目录存在
        const publicDir = path.join(projectRoot, 'public');
        if (!fs.existsSync(publicDir)) {
            console.log(`创建public目录: ${publicDir}`);
            fs.mkdirSync(publicDir, { recursive: true });
        }

        console.log(`开始扫描: ${srcDir}`);
        const components = scanComponents(srcDir);

        const componentManifest = {
            scanTime: new Date().toISOString(),
            scanPath: srcDir,
            projectRoot: projectRoot,
            components: components
        };

        // 写入manifest文件
        const manifestPath = path.join(publicDir, 'component-manifest.json');
        fs.writeFileSync(manifestPath, JSON.stringify(componentManifest, null, 2));

        console.log('\n扫描完成!');
        console.log(`扫描统计:`);
        console.log(`  项目根目录: ${projectRoot}`);
        console.log(`  扫描目录: ${srcDir}`);
        console.log(`  找到组件: ${components.length} 个`);
        console.log(`  Manifest文件: ${manifestPath}`);

        if (components.length > 0) {
            console.log('\n找到的组件列表:');
            components.forEach((comp, index) => {
                console.log(`${index + 1}. ${comp.name}`);
                console.log(`   路径: ${comp.filePath}`);
                console.log(`   默认导出: ${comp.hasDefaultExport ? '是' : '否'}`);
                console.log(`   命名导出: ${comp.exports.length > 0 ? comp.exports.join(', ') : '无'}`);
                console.log('');
            });
        } else {
            console.log('\n未找到任何React组件');
            console.log('可能的原因:');
            console.log('1. 项目结构不同，请检查src目录位置');
            console.log('2. 组件文件扩展名不是.tsx或.jsx');
            console.log('3. 组件不符合React组件的识别条件');
        }

    } catch (error) {
        console.error('扫描失败:', error);
        console.error(error.stack);
    }
}

// 执行扫描
main();