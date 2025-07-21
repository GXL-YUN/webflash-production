package cn.gui.app.management.bean;

public class TreeNodeData {
    private String displayValue;  // 显示值（如 "北京"）
    private String realValue;     // 实际值（如 "beijing"）

    public TreeNodeData(String displayValue, String realValue) {
        this.displayValue = displayValue;
        this.realValue = realValue;
    }

    // 重写 toString() 方法，让 JTree 显示 displayValue
    @Override
    public String toString() {
        return displayValue;
    }

    // Getter 方法
    public String getDisplayValue() { return displayValue; }
    public String getRealValue() { return realValue; }
}