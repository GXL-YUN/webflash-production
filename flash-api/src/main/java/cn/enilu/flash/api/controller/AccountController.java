package cn.enilu.flash.api.controller;

import cn.enilu.flash.api.model.UserWxml;
import cn.enilu.flash.api.utils.ApiConstants;
import cn.enilu.flash.api.utils.CookieUtil;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.constant.state.ManagerStatus;
import cn.enilu.flash.bean.core.ShiroUser;
import cn.enilu.flash.bean.dto.LoginDto;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.page.RequertInfo;
import cn.enilu.flash.bean.util.WxBean;
import cn.enilu.flash.bean.vo.SpringContextHolder;
import cn.enilu.flash.bean.vo.front.Ret;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.node.RouterMenu;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.cache.TokenCache;
import cn.enilu.flash.core.log.LogManager;
import cn.enilu.flash.core.log.LogTaskFactory;
import cn.enilu.flash.dao.system.UserRepository;
import cn.enilu.flash.security.JwtUtil;
import cn.enilu.flash.security.ShiroFactroy;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.service.system.MenuService;
import cn.enilu.flash.service.system.QrcodeService;
import cn.enilu.flash.service.system.UserService;
import cn.enilu.flash.utils.*;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.kmss.bean.entity.AnnouncementBean;

import cn.enilu.project.bean.model.ProjectModel;
import cn.enilu.project.dao.ProjectDao;
import cn.enilu.wx.util.WxUtil;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONObject;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.json.Json;
import org.nutz.mapl.Mapl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * AccountController
 *
 *
 * 用户管理 (普通用户  管理员数据)     商户管理(繁育商家   洗护商家   以上都是)   超级管理员
 * @author enilu
 * @version 2018/9/12 0012
 */
@RestController
@Slf4j
@RequestMapping("/api/account/user")
public class AccountController extends  CrudController<User, String, UserRepository, RequertInfo> {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);


    @Autowired
    private UserService userService;
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private MenuService menuService;
    @Autowired
    QrcodeService qrcodeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.wx.appid}")
    private  String appid;

    @Value("${spring.wx.secret}")
    private  String secret;
    private   String GXL_USER_LOGIN_TOKE="GXL:USER:LOGIN:TOKE:PREVIEW:";

    @Override
    protected BaseService<User, String, UserRepository>  getService() {
        return userService;
    }


    /**
     * 校验toke是否有效
     */

    @GetMapping(value = "/checkToken")
    public Object test(@RequestParam() String userKey) {

        Map<String, Object> resout=new HashMap<>();
     //   String userKey = CookieUtil.getCookie(request, "user_key");
        if (userKey == null) {
            resout.put("msg","未登录");
            resout.put("code",false);
        }else{
            String token = userKey;
            log.debug("验证Token: {}", token.substring(0, Math.min(20, token.length())) + "...");
            // 解密获得username
            String username = JwtUtil.getUsername(token);
            if (username == null) {
                resout.put("msg","Token解析失败，无法获取username");
                resout.put("code",false);
            }
            log.info("Token解析成功，username: {}", username);
            // 查询用户
            UserService operationLogRepository = SpringContextHolder.getBean(UserService.class);
            String id=  JwtUtil.getUserIdStr(token);
            User user = operationLogRepository.getById(id);
            resout.put("msg","验证成功");
            resout.put("date",user);
            resout.put("code",true);
        }
        return Rets.success(resout);
    }

    /**
    微信新增用户，更新用户
     */
    @PostMapping(value = "/verifyUser")
    public Object verifyUser(@RequestBody @Valid UserWxml userWxml) throws SQLException {
        //根据唯一编码查询数据
        Page<User> page = new PageFactory<User>().defaultPage();
        page.addFilter("phone", SearchFilter.Operator.EQ, "", SearchFilter.Join.and);
       // page = userService.queryPage(page);

        User main = userService.findByPhone(userWxml.getFdPhone());

        //roomListService.queryIndexNews();
        List<User> list = page.getRecords();
        //判断是否存在
        if (main!=null) {
            return Rets.failure("手机号重复");
        } else {
            User user = new User();
            user.setAccount(userWxml.getFdName());//账号
            user.setFdWxmlCode(userWxml.getWxmlCode());//微信code

            String salt = RandomUtil.getRandomString(5);
            user.setSalt(salt);
            user.setPassword(MD5.md5("12345", salt));//默认密码
            user.setStatus(1);
            user.setRoleid(",");
            user.setPhone(userWxml.getFdPhone());//手机号
            user.setEmail(userWxml.getFdEmail());//邮箱

            //获取微信
           // WxBean wx = new WxUtil().getAppId(userWxml.getWxmlCode(), appid, secret);

            //user.setSession_key(wx.getSession_key());
            //user.setOpenid(wx.getOpenid());
            List<User> listUser = new ArrayList<User>();
            listUser.add(user);
            userService.insert(user);
            return Rets.success(null);
        }
    }


    /**
     * 用户登录<br>
     * 1，验证没有注册<br>
     * 2，验证密码错误<br>
     * 3，登录成功
     *
     * @param loginDto
     * @return
     */
    @PostMapping(value = "/login")
    public Object login(@RequestBody LoginDto loginDto,HttpServletRequest request, HttpServletResponse response) {
        try {
            //1,
            String password = loginDto.getPassword();
            String userName = loginDto.getUsername();

           String code= CryptUtil.encrypt( password);
            password = CryptUtil.desEncrypt(code);
            User user = userService.findByAccountForLogin(userName);
            if (user == null) {
                return Rets.failure("用户名不存在");
            }
            if (user.getStatus() == ManagerStatus.FREEZED.getCode()) {
                return Rets.failure("用户已冻结");
            } else if (user.getStatus() == ManagerStatus.DELETED.getCode()) {
                return Rets.failure("用户已删除");
            }
            String passwdMd5 = MD5.md5(password, user.getSalt());
            //2,
            if (!user.getPassword().equals(passwdMd5)) {
                return Rets.failure("用户名或密码错误");
            }
            if (StringUtil.isEmpty(user.getRoleid())) {
                return Rets.failure("该用户未配置权限");
            }
            String token = userService.loginForToken(user);
            //ShiroFactroy.me().shiroUser(token, user);
            Map<String, String> result = new HashMap<>(1);
            result.put("token", token);

            //存储用户登录用户toke  用于后续接口响应
            redisTemplate.opsForValue().set(GXL_USER_LOGIN_TOKE+user.getPhone(), token);

            CookieUtil.setCookie(response,request,"user_key",token,10000000);

            result.put("user_key", token);
            CookieUtil.setCookie(response,request,"user_id",user.getFdId(),10000000);

            result.put("user_id", user.getFdId());
            CookieUtil.setCookie(response,request,"isAdmin",user.getFdType(),10000000);
            result.put("isAdmin", user.getFdType());

            LogManager.me().executeLog(LogTaskFactory.loginLog(user.getId(), HttpUtil.getIp()));
            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }


    /**
     * 根据手机号登录账号
     * @return
     */
    @PostMapping(value = "/loginWxml")
    public Object loginWxml(@RequestBody @Valid UserWxml loginDto) {
        try {
            //1,
            String fdPassword = loginDto.getFdPassword();
            String fdPhone = loginDto.getFdPhone();
            String code= CryptUtil.encrypt( fdPassword);
            User user = userService.findByPhone(code);
            if (user == null) {
                return Rets.failure("用户不存在");
            }
            if (user.getStatus() == ManagerStatus.FREEZED.getCode()) {
                return Rets.failure("用户已冻结");
            } else if (user.getStatus() == ManagerStatus.DELETED.getCode()) {
                return Rets.failure("用户已删除");
            }
            String passwdMd5 = MD5.md5(fdPassword, user.getSalt());
            //2,
            if (!user.getPassword().equals(passwdMd5)) {
                return Rets.failure("用户名或密码错误");
            }
            String token = userService.loginForToken(user);
            ShiroFactroy.me().shiroUser(token, user);
            Map<String, Object> result = new HashMap<>(1);
            result.put("token", token);
            result.put("user", user);
            Object wx= new WxUtil().getAppId(loginDto.getWxmlCode());
            result.put("boby",wx );
            result.put("wxCode",loginDto.getWxmlCode() );
            //存储登录用户
            redisTemplate.opsForValue().set(GXL_USER_LOGIN_TOKE+user.getId(), result);

            //判断是否是管理员
            if(user.getAccount().equals("admin")){
                result.put("isAdmin", "true");
            }else{
                result.put("idAdmin", "false");
            }
            LogManager.me().executeLog(LogTaskFactory.loginLog(user.getId(), HttpUtil.getIp()));
            return Rets.success(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("登录时失败");
    }


    /**
     * 用户基本信息
     * @return
     */
    @GetMapping(value = "/info")
    public Object info() {
        HttpServletRequest request = HttpUtil.getRequest();
        String idUser = null;
        try {
            idUser = getIdUser(request);
        } catch (Exception e) {
            return Rets.expire();
        }
        if (idUser != null) {
            User user = userService.get(idUser);
            if (user == null) {
                //该用户可能被删除
                return Rets.expire();
            }
            if (StringUtil.isEmpty(user.getRoleid())) {
                return Rets.failure("该用户未配置权限");
            }
            ShiroUser shiroUser = tokenCache.getUser(getToken());
            Map map = Maps.newHashMap("name", user.getName(), "role", "admin", "roles", shiroUser.getRoleCodes());
            List<RouterMenu> list = menuService.getSideBarMenus(shiroUser.getRoleList());
            map.put("menus", list);
            map.put("permissions", shiroUser.getUrls());

            Map profile = (Map) Mapl.toMaplist(user);
            profile.put("dept", shiroUser.getDeptName());
            profile.put("roles", shiroUser.getRoleNames());
            map.put("profile", profile);

            return Rets.success(map);
        }
        return Rets.failure("获取用户信息失败");
    }


    /**
     * 判断用户是否是管理员
     * @return
     */

    @GetMapping(value = "/getUser")
    public Object getUser() {
        HttpServletRequest request = HttpUtil.getRequest();
        boolean  flage=false;
        String idUser = null;
        try {
            idUser = getIdUser(request);
        } catch (Exception e) {
           e.printStackTrace();
        }
        if (idUser != null) {
            User user = userService.get(idUser);
            if(user.getAccount().equals("admin")){
                flage=true;
            }

        }
        return Rets.success(flage);
    }

    /**
     * 判断用户是否登录
     * @return
     */
    @GetMapping(value = "/isLogin")
    public Object isLogin() {
        HttpServletRequest request = HttpUtil.getRequest();

        String idUser = null;
        try {
            idUser = getIdUser(request);
        } catch (Exception e) {
            return Rets.expire();
        }
        return Rets.success();
    }

    @PostMapping(value = "/updatePwd")
    public Object updatePwd(String oldPassword, String password, String rePassword) {
        try {

            if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword)) {
                return Rets.failure("密码不能为空");
            }
            if (!password.equals(rePassword)) {
                return Rets.failure("新密码前后不一致");
            }
            User user = userService.get(getIdUser(HttpUtil.getRequest()));
            if (ApiConstants.ADMIN_ACCOUNT.equals(user.getAccount())) {
                return Rets.failure("不能修改超级管理员密码");
            }
            if (!MD5.md5(oldPassword, user.getSalt()).equals(user.getPassword())) {
                return Rets.failure("旧密码输入错误");
            }

            user.setPassword(MD5.md5(password, user.getSalt()));
            userService.update(user);
            return Rets.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Rets.failure("更改密码失败");
    }

    /**
     * 生成登录二维码（PC端调用）
     *
     * @param response
     */
    @GetMapping("/qrcode/generate")
    public void generateQrcode(@RequestParam("uuid") String uuid,
                               HttpServletResponse response) {
        BitMatrix bitMatrix = qrcodeService.createQrcode(uuid);

        response.setContentType("image/jpg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "jpg", stream);
        } catch (IOException e) {
            logger.error("generate QrCode error", e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                logger.error("close stream error", e);
            }
        }

    }

    /**
     * 获取二维码扫码结果(PC端调用）
     *
     * @return
     */
    @GetMapping("/qrcode/getRet")
    public Ret getQrcodeStatus(@RequestParam("uuid") String uuid) {
        String ret = qrcodeService.getCrcodeStatus(uuid);
        if (QrcodeService.INVALID.equals(ret)) {
            return Rets.success(Maps.newHashMap("status", ret, "msg", "二维码已过期"));
        }
        if (QrcodeService.CANCEL.equals(ret)) {
            return Rets.success(Maps.newHashMap("status", ret, "msg", "已取消登录"));

        }
        if (QrcodeService.UNDO.equals(ret)) {
            return Rets.success(Maps.newHashMap("status", ret, "msg", "待扫描"));
        }
        Map map = Json.fromJson(Map.class, ret);
        return Rets.success(map);
    }

    /**
     * @param account 用户账号
     * @param qrcode  二维码值
     * @param confirm 是否确认登录：1:是,0:否
     * @return
     */
    @PostMapping("/qrcode/login")
    public Ret qrLogin(@RequestParam("phone") String phone,
                       @RequestParam("qrcode") String qrcode,
                       @RequestParam("confirm") String confirm
    ) {
        String qrstatus = qrcodeService.getCrcodeStatus(qrcode);
        if (QrcodeService.INVALID.equals(qrstatus)) {
            return Rets.failure("二维码已过期");
        }
        if (QrcodeService.SUCCESS.equals(qrstatus) || QrcodeService.CANCEL.equals(qrstatus)) {
            return Rets.failure("二维码已被他人使用");
        }
        if (QrcodeService.UNDO.equals(qrstatus)) {
            qrcodeService.login(phone, qrcode, confirm);
            return Rets.success();
        } else if (QrcodeService.INVALID.equals(qrstatus)) {
            return Rets.failure("二维码已过期");
        } else {
            return Rets.failure("无效的二维码");
        }


    }


}
