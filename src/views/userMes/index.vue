<template>
    <el-form ref="userForm" :model="userForm" label-width="120px" class="user-form">
      <el-form-item label="用户ID">
        <el-input v-model="userForm.userId" disabled></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="userForm.userName" :disabled="isDisabled"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="userForm.userPassword" :disabled="isDisabled"></el-input>
      </el-form-item>
      <el-form-item label="电子邮箱">
        <el-input v-model="userForm.userEmail" :disabled="isDisabled"></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="userForm.userPhone" :disabled="isDisabled"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm" v-if="!isDisabled">提交</el-button>
        <el-button @click="resetForm" v-if="!isDisabled">重置</el-button>
        <el-button @click="enableEditing" v-else>修改</el-button>
      </el-form-item>
    </el-form>
  </template>
  
  <script>
import { method } from 'lodash';

  export default {
    data() {
      return {
        userId: localStorage.getItem("userId"),
        userType: localStorage.getItem("userType"),
        userName: localStorage.getItem("userName"),
        userForm: {
          userId: '',
          userName: '',
          userPassword: '',
          userType: 1,
          userEmail: '',
          userPhone: ''
        },
        isDisabled: true // 初始化时禁用编辑
      };
    },
    created(){
      this.initData();
    },
    methods: {
      initData(){
        var formData = new FormData();
        formData.append("userId",this.userId);
        var options = {
          url:"http://localhost:8080/bj/user/getUserMes",
          method:"post",
          data:formData,
        }
        this.$axios(options)
            .then(response=>{
              console.log("userIdMes")
              console.log(response);
              var data = response.data.data;
              this.userForm.userId = this.userId;
              this.userForm.userPassword = data.userPassword;
              this.userForm.userName = data.userName;
              this.userForm.userEmail = data.userEmail ;
              this.userForm.userPhone = data.userPhone ;
            })
            .catch(function(error){
              console.log(error);
            })
      },
      submitForm() {
        this.$refs.userForm.validate((valid) => {
          if (valid) {
            var formData = new FormData();
            formData.append("userId",this.userId);
            formData.append("userName",this.userForm.userName);
            formData.append("userPassword",this.userForm.userPassword);
            formData.append("userEmail",this.userForm.userEmail);
            formData.append("userPhone",this.userForm.userPhone);
            formData.append("userType",this.userType);
            var options = {
              url:"http://localhost:8080/bj/user/updateUserMes",
              method:"post",
              data:formData,
            }
            this.$axios(options)
                .then(response=>{
                  console.log("updateMes")
                  console.log(response);
                  this.$message({
                  message: '修改成功',
                  type: 'success'
                })
                })
                .catch(function(error){
                  console.log(error);
                })
            // alert('提交成功!');
            this.isDisabled = true; // 提交后再次禁用表单
            this.initData();
          } else {
            console.log('提交错误!!');
            return false;
          }
        });
      },
      resetForm() {
        this.$refs.userForm.resetFields();
      },
      enableEditing() {
        this.isDisabled = false; // 激活编辑模式
      }
    }
  }
  </script>
  
  <style scoped>
  .user-form {
    width: 60%;
    margin: 50px auto;
  }
  </style>