package cn.enilu.flash.security;

/**
 * 权限常量
 */
public final class PermConstant {

    private PermConstant() {
        // 工具类，防止实例化
    }

    // ========== 系统管理 ==========
    public static final String SYS_USER_MANAGE = "sys:user:manage";
    public static final String SYS_ROLE_MANAGE = "sys:role:manage";
    public static final String SYS_DEPT_MANAGE = "sys:dept:manage";
    public static final String SYS_MENU_MANAGE = "sys:menu:manage";

    // ========== 业务权限 ==========
    public static final String USER_VIEW = "user:view";
    public static final String USER_ADD = "user:add";
    public static final String USER_EDIT = "user:edit";
    public static final String USER_DELETE = "user:delete";
    public static final String USER_EXPORT = "user:export";

    // ========== 通配符权限 ==========
    public static final String USER = "user:*";
    public static final String ADMIN = "admin:*";

    // ========== 其他 ==========
    public static final String LOG_VIEW = "log:view";
    public static final String DASHBOARD_VIEW = "dashboard:view";
}