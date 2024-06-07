<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left" >
      <!-- <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left"> -->
      <div class="title-container">
        <h3 class="title">欢迎登录离散数学课程学习平台</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="Username"
          name="username"
          type="text"
          tabindex="1"
          autocomplete="on"
        />
      </el-form-item>

      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="Password"
            name="password"
            tabindex="2"
            autocomplete="on"
            @keyup.native="checkCapslock"
            @blur="capsTooltip = false"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
      </el-tooltip>
      <!-- 登录 -->
      <el-row>
        <el-button :loading="loading" type="primary" style="width:45%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
        <span v-html="'\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0'"></span>
        <el-button :loading="loading" type="primary" style="width:45%;margin-bottom:30px;" @click="DialogFormVisible=true">注册</el-button>
      </el-row>
      <!-- <div style="position:relative">
        <div class="tips">
          <span>Username : admin</span>
          <span>Password : any</span>
        </div>
        <div class="tips">
          <span style="margin-right:18px;">Username : editor</span>
          <span>Password : any</span>
        </div>

        <el-button class="thirdparty-button" type="primary" @click="showDialog=true">
          Or connect with
        </el-button>
      </div> -->
    </el-form>

    <el-dialog class="selfDialog" :visible.sync="DialogFormVisible">
      <el-form :model="registerForm" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="账号" prop="userId" >
          <el-input v-model="registerForm.userId" />
        </el-form-item>
        <el-form-item label="密码" prop="userPassword" >
          <el-input v-model="registerForm.userPassword"  show-password/>
        </el-form-item>
        <el-form-item label="用户名" prop="userName" >
          <el-input v-model="registerForm.userName"  />
        </el-form-item>
        <el-form-item label="邮箱" prop="userEmail" >
          <el-input v-model="registerForm.userEmail"  />
        </el-form-item>
        <el-form-item label="联系方式" prop="userPhone" >
          <el-input v-model="registerForm.userPhone"  />
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <!-- <span>test</span> -->
          <el-select v-model="registerForm.userType">
            <el-option
              v-for="item in elOptons"
              :key="item.value"
              :label="item.label"
              :value="item.value">
              <!-- @click.native="changeTestType(item)" -->
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelR()">
          取消
        </el-button>
        <el-button type="primary" @click="handleRegister()">
          注册
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
import SocialSign from './components/SocialSignin'

export default {
  name: 'Login',
  components: { SocialSign },
  data() {
    const validateUsername = (rule, value, callback) => {
      validUsername(value)
      callback();
      // if (!validUsername(value)) {
      //   callback(new Error('Please enter the correct user name'))
      // } else {
      //   callback()
      // }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 1) {
        callback(new Error('密码长度不能低于6位'))
      } else {
        callback()
      }
    }
    return {
      elOptons:[{
        value:1,
        label:'教师'
        },{
          value:2,
          label:'学生'
        },
      ],
      DialogFormVisible:false,
      registerForm:{
        userId:'',
        userName:'',
        userPassword:'',
        userType:'',
        userEmail:'',
        userPhone:''
      },
      loginForm: {
        username: '',
        password: '',
        usertype: 0
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    cancelR(){
      this.DialogFormVisible = false;
      this.registerForm.userId='';
      this.registerForm.userName='';
      this.registerForm.userPassword='';
      this.registerForm.userPhone='';
      this.registerForm.userType='';
      this.registerForm.userEmail='';
    },
    handleRegister(){
      var options={
        url:'http://localhost:8080/bj/user/addUser',
        method:'post',
        data:{userId:this.registerForm.userId,
              userName:this.registerForm.userName,
              userPassword:this.registerForm.userPassword,
              userPhone:this.registerForm.userPhone,
              userType:this.registerForm.userType,
              userEmail:this.registerForm.userEmail,}
      }
      this.$axios(options)
          .then(response=>{
            console.log("register")
            console.log(response);
            if(response.data.data == 6){
              this.$message({
                message: '注册成功请登录',
                type: 'success'
              })
            }else{
              this.$message({
                message: '注册失败',
                type: 'danger'
              })
            }
          })
          .catch(function(error){
            console.log(error);
          })
    },
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin(){
      // console.log(this.$axios.version);
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          console.log('test....');
          console.log(this.loginForm.username);
          console.log('post....');
          // console.log(formData);
          var options = {
            url: 'http://localhost:8080/bj/user/login',
            data:{userId:this.loginForm.username,userPassword:this.loginForm.password},
            method: 'post'
            
          }
          this.$axios(options)
          .then(response =>{
            console.log(response);
            console.log(response.data.msg);

            if(response.data.msg==="操作成功!"){
              this.loginForm.username = response.data.data.userId;
              this.loginForm.userType = response.data.data.userType;
              var uid = response.data.data.userId;
              var utype = response.data.data.userType;
              // console.log("loginform...")
              console.log(uid)
              //登陆成功后先store保存 再跳转  if user.type
              this.loading = true
              this.$store.dispatch('user/login', this.loginForm)
                .then(() => {
                  console.log("turning..")
                  //传递数据
                  localStorage.setItem('userId',uid)
                  localStorage.setItem('userType',utype)
                  localStorage.setItem('userName',response.data.data.userName);
                  //接受数据
                  // localStorage.getItem("data")
                  this.$router.push({ path: this.redirect || '/', query: {userId:uid,userType: utype} })
                  this.loading = false
                })
                .catch(() => {
                  this.loading = false
                })
            }else{
              alert("用户名或密码错误！")
            }
          })
        .catch(function(error){
          console.log(error);
        })
      }})
    
      // var formData = new FormData();
      // formData.append("userId",this.loginForm.username);
      // formData.append("userPassword",this.loginForm.password);

    },
    // headers: {'content-type ':' application/json '}
// @RequestBody 用 content-type = application/json 
// @RequestParam 用  content-type = application/x-www-form-urlencoded
//这个问题报错好久！！！！
//     async handleLogin() {
//       var formData = new FormData();
//       formData.append("userId",this.loginForm.username);
//       formData.append("userPassword",this.loginForm.password);
//       console.log(this.loginForm.username);
//       console.log('post...')
//       console.log(formData)
//       const ctrl = new AbortController();
//       setTimeout(()=>ctrl.abort(),5000);
//       try{
//         // var api = "";
//         const response = await fetch("http://127.0.0.1:8080/"+"bj/user/login",{
//           method:"POST",
//           body:JSON.stringify(formData),
//           headers: {
//                 'Content-Type': 'application/json;charset=utf-8',
//                 // 'Accept': 'application/json'
//           },
// //@RequestBody 用 content-type = application/json 
// //@RequestParam 用  content-type = application/x-www-form-urlencoded
//           signal:ctrl.signal,
//         });
//         if (!response.ok) {
//           throw new Error(`HTTP异常:${response.status}`);
//         }
//         var data = await response.json();
//         console.log(data);

//       }catch(e){
//         console.log(e);
//       }
//     },
    // handleLogin() {
    //   this.$refs.loginForm.validate(valid => {
    //     if (valid) {
    //       this.loading = true
    //       this.$store.dispatch('user/login', this.loginForm)
    //         .then(() => {
    //           this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
    //           this.loading = false
    //         })
    //         .catch(() => {
    //           this.loading = false
    //         })
    //     } else {
    //       console.log('error submit!!')
    //       return false
    //     }
    //   })
    // },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
    // afterQRScan() {
    //   if (e.key === 'x-admin-oauth-code') {
    //     const code = getQueryObject(e.newValue)
    //     const codeMap = {
    //       wechat: 'code',
    //       tencent: 'code'
    //     }
    //     const type = codeMap[this.auth_type]
    //     const codeName = code[type]
    //     if (codeName) {
    //       this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
    //         this.$router.push({ path: this.redirect || '/' })
    //       })
    //     } else {
    //       alert('第三方登录失败')
    //     }
    //   }
    // }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}


/* reset element-ui css */
// .Registerdialog{
//   .el-input {
//     display: inline-block;
//     height: 47px;
//     width: 85%;

//     input {
//       background: #fff;
//       border: 0px;
//       -webkit-appearance: none;
//       border-radius: 0px;
//       padding: 12px 5px 12px 15px;
//       color: #000;
//       height: 47px;
//       caret-color: $cursor;

//       &:-webkit-autofill {
//         box-shadow: 0 0 0px 1000px $bg inset !important;
//         -webkit-text-fill-color: $cursor !important;
//       }
//     }
//   }

//   .el-form-item {
//     border: 1px solid rgb(255, 255, 255);
//     background: rgb(255, 255, 255);
//     border-radius: 5px;
//     color: #000000;
//   }
// }
.selfDialog{
  height: auto;
  width: 70%;
  position:absolute;
  left: 15%;
  // top:50%;
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
    border: 5px;
    border-radius: 30px;

    input {
      // background: transparent;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: #000000;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }
  .el-form-item {
    border: 0px solid rgb(82, 79, 79);
    background: rgb(255, 255, 255);
    border-radius: 5%;
    color: #000000;//字体颜色
  }
}
.login-form {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}

</style>

