// 引入mockjs
const Mock = require('mockjs')

// 获取 mock.Random 对象
const Random = Mock.Random

let Result = {
    code: 200,
    msg: '操作成功！',
    data: null
}

/**
 * Mock.mock( url, post/get , function(options))；
 * url 表示需要拦截的 URL，
 * post/get 需要拦截的 Ajax 请求类型
 *
 * 用于生成响应数据的函数
 */
// 获取验证码图片base64编码以及一个随机码
Mock.mock('/captcha', 'get', () => {
    Result.data = {
        // 获取一个32位的随机字符串
        token: Random.string(32),
        //生成验证码为11111的base64图片编码
        captchaImg: Random.dataImage( "120x40", "11111" )
    }
    return Result
})

// 因为mock不认识/login?username=xxx，所以用了正则表达式
Mock.mock(RegExp('/login'), 'post', (config) => {
    // 这里无法在header添加authorization，直接跳过
    console.log("mock----------------login")
    Result.code = 400;
    Result.msg = '验证码错误！';
    return Result
})

// 获取用户信息
Mock.mock(RegExp('/system/userInfo'), 'get', (config) => {
    Result.data = {
        id: '1',
        username: '代小飞',
        avatar: 'http://vue.ruoyi.vip/static/img/profile.473f5971.jpg'
    }
    return Result
})

// 获取用户信息
Mock.mock(RegExp('/logout'), 'get', (config) => {
    Result.code = 200;
    return Result
})

// 获取用户菜单以及权限接口
Mock.mock(RegExp('/system/menu/nav'), 'get', () => {
    // 导航菜单json
    let nav = [
        {
            name: 'SystemManager',
            path: '',
            component: '',
            title: '系统管理',
            icon: 'el-icon-s-operation',
            children: [
                {
                    name: 'SystemUser',
                    path: '/system/user',
                    component: 'system/user/User',
                    title: '用户管理',
                    icon: 'el-icon-s-custom',
                    children: []
                },
                {
                    name: 'SystemRole',
                    path: '/system/role',
                    component: 'system/role/Role',
                    title: '角色管理',
                    icon: 'el-icon-rank',
                    children: []
                },
                {
                    name: 'SystemMenu',
                    path: '/system/menu',
                    component: 'system/menu/Menu',
                    title: '菜单管理',
                    icon: 'el-icon-menu',
                    children: []
                }
            ]
        },
        {
            name: 'SystemTools',
            path: '',
            component: '',
            title: '系统工具',
            icon: 'el-icon-s-tools',
            children: [
                {
                    name: 'SystemDict',
                    path: '/system/dict',
                    component: 'system/dict/Dict',
                    title: '数字字典',
                    icon: 'el-icon-s-custom',
                    children: []
                }
            ]
        }
    ]

    // 权限信息
    // let authoritys = ['system:user:list', "system:user:save", "system:user:delete"]
    let authoritys = []

    Result.data = {
        nav: nav,
        authoritys: authoritys
    }
    return Result
})