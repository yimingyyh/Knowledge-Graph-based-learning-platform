
<template>
  <div class="kgContainer">
    <!-- <div class="alert alert-info" role="alert" style="margin-bottom: 20px;">
      <h1>Prompt</h1>
      <p size="4">输入你想要搜索的知识</p>
    </div> -->
    <div class="search" style="text-align: center;margin-bottom: 50px;" v-show="userType==1">
      <!-- <el-input v-model="entityName" placeholder="请输入内容" style="width: 150px;"></el-input> -->
      <!-- <el-button type="primary" icon="el-icon-search" >搜索</el-button> -->
      <el-select v-model="elOptionValue" placeholder="请选择">
        <el-option
          v-for="item in elOptons"
          :key="item.value"
          :label="item.label"
          :value="item.value"
          @click.native="changeAddStatus(item)"
          >
        </el-option>
      </el-select>
      <el-input v-model="form.entityFrom" placeholder="父课程名" style="width: 100px;"></el-input>
      <!-- <el-input v-model="form.relation" placeholder="请输入内容" style="width: 100px;"></el-input> -->
      <el-input v-model="form.entityTo" placeholder="子课程名" style="width: 150px;" v-show="showStatus1"></el-input>
      <el-button type="primary" icon="el-icon-Plus" @click="add" >添加</el-button>
      <el-button type="primary" icon="el-icon-delete" @click="deleteCourse" v-show="!showStatus1">删除课程</el-button>

    </div>
    <div class="kgShow" id="kgShow" style="width: 100%; height:800px;">
      
    </div>
    <el-drawer
      :title="drawerTitle"
      :visible.sync="drawer"
      :size="'40%'"
      :direction="direction"
      :before-close="handleClose">
      
      <!-- 简介 ppt 视频 练习题 -->
      <el-form :model="courseInformationForm" status-icon :rules="courseTableRules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <!-- <el-form-item label="简介" prop="courseIntroduction">
          <el-input  type="textarea" :autosize="{ minRows:1, maxRows: 6}" size="small" v-model="courseInformationForm.courseIntroduction" autocomplete="off"></el-input>
        </el-form-item> -->
        <el-form-item label="简介" prop="courseIntroduction">
          <el-input  type="textarea"  maxlength="200" show-word-limit  v-model="courseInformationForm.courseIntroduction" autocomplete="off"></el-input>
          <el-button size="small" type="primary" @click="updateIntroduction" v-if="userType == 1">点击上传</el-button>
        </el-form-item>
        <el-form-item label="课程ppt" prop="coursePptpath" :inline="true">
          <el-input  type="text" style="width:300px" v-model="courseInformationForm.coursePptpath" autocomplete="off"></el-input>
          <el-upload
          class="upload-demo"
          action="http://localhost:8080/bj/courseInformation/saveFile"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          multiple
          :limit="3"
          :on-success="handleExceedPpt"
          :data="{savePath: this.Path.url,courseName:this.entityName,type:1}"
          :file-list="fileList"
          v-if="userType == 1">
          <el-button size="small" type="primary" >点击上传</el-button>
          <!-- <el-button size="small" type="primary" v-if="uploadStatus.ppt">点击上传</el-button>
          <el-button size="small" type="primary" v-else>点击修改</el-button> -->
          </el-upload>
          <el-button size="small" type="primary" v-else @click="downloadFile(courseInformationForm.coursePptpath)">点击下载</el-button>
        </el-form-item>
        <el-form-item label="课程练习" prop="coursePracticePath">
          <el-input type="text" v-model="courseInformationForm.coursePracticePath"></el-input>
          <el-upload
          class="upload-demo"
          action="http://localhost:8080/bj/courseInformation/saveFile"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          multiple
          :limit="3"
          :on-success="handleExceedPractice"
          :data="{savePath: this.Path.url,courseName:this.entityName,type:2}"
          :file-list="fileList" v-if="userType == 1">
          <el-button size="small" type="primary" >点击上传</el-button>
          <!-- <el-button size="small" type="primary" v-if="uploadStatus.practice">点击上传</el-button>
          <el-button size="small" type="primary" v-else>点击修改</el-button> -->
          </el-upload>
          <el-button size="small" type="primary" v-else @click="downloadFile(courseInformationForm.coursePracticePath)">点击下载</el-button>
        </el-form-item>
        <el-form-item label="课程视频" prop="courseVideopath">
          <el-input type="text" v-model="courseInformationForm.courseVideopath"></el-input>
          <el-upload 
            class="avatar-uploader el-upload--text"
            :drag="{Plus}"
            action="http://localhost:8080/bj/courseInformation/saveFile"
            multiple
            :show-file-list="false"
            :data="{savePath: this.Path.url,courseName:this.entityName,type:3}"
            :on-success="handleVideoSuccess"
            :before-upload="beforeUploadVideo"
            :on-progress="uploadVideoProcess" v-if="userType == 1">
            <i v-if="Plus" class="el-icon-upload"></i>
            <div v-if="Plus" class="el-upload__text">将视频拖到此处，或<em>点击上传</em></div>
            <el-progress v-if="videoFlag == true" type="circle" :percentage="videoUploadPercent" style="margin-top:30px;"></el-progress>
            <div class="el-upload__tip" slot="tip">只能上传mp4/flv/avi文件,且不超过300M</div>
          </el-upload>
          <el-button size="small" type="primary" v-else @click="downloadFile(courseInformationForm.courseVideopath)">点击下载</el-button>

        </el-form-item>
        <!-- <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item> -->
      </el-form>
      
    </el-drawer>
    <!--<div class="test" id="tst" style="width: 100%;height: 500px;"></div>-->
  </div>
</template>

<script>
import DropdownMenu from '@/components/Share/DropdownMenu'
import { Tooltip } from 'element-ui';
import * as echarts from "echarts";
import { Position } from 'driver.js';
import { color } from 'echarts/lib/export';
// var echarts = require('echarts');
export default {
  name: 'Documentation',
  components: { DropdownMenu },
  data() {
    return {
      fullPptPath:'',
      fullPracPath:'',
      fullVideoPath:'',
      userId: localStorage.getItem("userId"),
      userType: localStorage.getItem("userType"),
      userName: localStorage.getItem("userName"),
      videoForm: {
        videoId: '',
        videoUrl: ''
      },
      practiceForm: {
        practiceId: '',
        practiceUrl: ''
      },
      pptForm: {
        pptId: '',
        pptUrl: ''
      },
      videoUploadPercent: 0,
      videoFlag: false,
      Plus: true,
      Path: {
        url: 'F:/video/videoUpload'
      },
      uploadStatus:{
        ppt:true,//true 就是上传 false就是修改
        practice:true,
        video:true,
      },
      courses:[],
      elOptionValue:'',
      showStatus1:false,
      drawer: false,
      drawerTitle:'title',
      direction: 'rtl',
      entityName:'',
      option:'',
      form:{
        entityFrom:'',
        relation:'',
        entityTo:'',
      },
      courseInformationForm:{
        courseName:this.entityName,
        courseIntroduction:'',
        coursePptpath:'',
        coursePracticePath:'',
        courseVideopath:''
      },
      nodes:[],
      relationships:[],
      chartParam:'',
      changeColor:{
        "false": '#5470c6',
        "true" : '#91cc75'
      },
      elOptons:[{
        value:1,
        label:'添加课程'
        },{
          value:2,
          label:'添加子课程'
        }
      ],
      courseTableRules:{
        //填写课程信息的一些要求
        // name: [
        //     { required: true, message: '请输入活动名称', trigger: 'blur' },
        //     { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        // ],
        // region: [
        //   { required: true, message: '请选择活动区域', trigger: 'change' }
        // ],
        // date1: [
        //   { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
        // ],
        // date2: [
        //   { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
        // ],
      }
    }
  },
  mounted(){
    // console.log(this.changeColor['false']);
    this.initData();
  },
  created(){
    // this.initData();
  },
  // inject:['reload'],
  methods:{
    getFileName(filePath){
      // 假设filePath是你要处理的文件的绝对路径
      // const filePath = '/path/to/your/file/example.txt';

      // 使用split()方法将路径分割成数组
      const pathArray = filePath.split('/');

      // 获取文件名，即数组中的最后一个元素
      const fileName = pathArray[pathArray.length - 1];

      // 使用split()方法将文件名分割成数组
      const fileNameArray = fileName.split('.');

      // 获取文件后缀，即数组中的最后一个元素
      const fileExtension = fileNameArray[fileNameArray.length - 1];
      // name = fileName;
      console.log('文件名:', fileName); // 输出: example.txt
      console.log('后缀:', fileExtension); // 输出: txt
      return fileName;
    },
    downloadFile(fileName){
      console.log("downloading....");
      // 使用split()方法将文件名分割成数组
      const fileNameArray = fileName.split('.');
      // 获取文件后缀，即数组中的最后一个元素
      const fileExtension = fileNameArray[fileNameArray.length - 1];
        // 如果文件是 MP4 类型，则直接构造文件路径
      if (fileExtension === 'mp4') {
        const filePath = require('@/assets/learningSources/' + fileName);
        
        // 创建下载链接
        const link = document.createElement('a');
        link.href = filePath;
        link.download = fileName;
        
        // 将链接添加到页面并模拟点击
        document.body.appendChild(link);
        link.click();
        
        // 清理
        document.body.removeChild(link);
      } else {
        // 否则，使用 require.context 来获取文件路径
        const files = require.context('@/assets/learningSources', false, /\.(txt|pdf|doc|docx|xls|xlsx)$/);
        const file = files(`./${fileName}`);    
        // const filePath = file.default;
        
        // 创建下载链接
        const link = document.createElement('a');
        link.href = file.default; // 获取文件路径   
        link.download = fileName;
        
        // 将链接添加到页面并模拟点击
        document.body.appendChild(link);
        link.click();
        
        // 清理
        document.body.removeChild(link);
      }
      // let files = null;
      // if(fileExtension == 'mp4')
      //   files = require.context('@/assets/learningSources', false, /\.(mp4)$/); 
      // else
      //   files = require.context('@/assets/learningSources', false, /\.(txt|pdf|doc|docx|xls|xlsx)$/);   
      //  // 使用 require.context 函数将文件打包成模块
      // //  const files = require.context('@/assets/learningSources', false, /\.(txt|pdf|doc|docx|mp4|xls|xlsx)$/);     
      // // 尝试获取文件模块
      // const file = files(`./${fileName}`);      
      // // 创建一个 <a> 元素
      // const link = document.createElement('a');
      // link.href = file.default; // 获取文件路径      
      // // 设置下载的文件名 
      // link.download = fileName;    
      // // 将 <a> 元素添加到页面上，并模拟点击
      // document.body.appendChild(link);
      // link.click();
      // // 清理
      // document.body.removeChild(link);

    },
    deleteCourse(){ 
      var formData = new FormData();
      formData.append("courseName",this.form.entityFrom)
      var options = {
            url: 'http://localhost:8080/bj/kg/deleteNodeAndRelation',
            method: 'post',
            data: formData
          }
      this.$axios(options)
          .then(response=>{
            console.log("deleteNodeAndRelation");
            console.log(response);
            this.$message({
                message: '删除成功',
                type: 'success'
            })
            this.initData();
          })
          .catch(function(error){
            console.log(error);
          })
    },
    initData(){
      var formData = new FormData();
      formData.append("userId",this.userId);
      var options = {
            url: 'http://localhost:8080/bj/kg/getAllCourse',
            method: 'post',
            data:formData,
          }
      this.$axios(options)
        .then(response =>{
          console.log(response);
          var kg = response.data.data;
          console.log("kg#######")
          console.log(kg)
          // console.log(this.changeColor[kg[0]["finished"]])
          // console.log(kg[0]['name'])
          const courseList = [];
          for(let i = 0;i<kg.length;i++){
            // var status = kg[i]["finished"];
            // var t='';
            // if(status=='true')
            //   t = '#4dff7f';
            // else
            //   t = '#4b565b';
            // console.log(status)
            var color = '#5470c6';
            if(this.userType == 2){
              color = this.changeColor[kg[i]["finished"].toString()];
            }
            const temp = {
              name: kg[i]["name"],
              symbolSize:70,
              itemStyle:{
                normal:{
                  //放在normal里面 放外面会失效
                  // color: '#4dff7f',
                  color: color,
                  show:true,
                  shadowBlur: 20,
                  shadowColor: 'rgba(0, 0, 0, 0.3)', // 灰色阴影，透明度降低
                  borderWidth: 0, // 默认无边框
                },
                emphasis: {
                    borderColor: 'rgba(255, 192, 159, 0.5)', // 淡橙色边框
                    borderWidth: 10,
                }
              }
            }
            courseList.push(temp);
          }
          this.courses = courseList;
          console.log("courseList:")
          console.log(courseList)
          const links = [];
          for(let i = 0;i<kg.length;i++){
            for(let j = 0;j<kg[i]["branches"].length;j++){
                const temp = {
                  source:kg[i]["name"],
                  target:kg[i]["branches"][j]["name"],
                  // name:'going',
                  // label: {
                  //   show: true
                  // }
                }
                links.push(temp);
            }
          }
          console.log("links:")
          console.log(links)
          const option = {
            title: {
              text: "离散数学"
            },
            tooltip: {
              formatter: function(x) {
                return x.data.name;
              }
            },
            animationDurationUpdate: 1500,
            animationEasingUpdate: 'quinticInOut',
            series: [{
              type: 'graph',
              layout: 'force',
              symbolSize: 30,
              roam: true,
              label: {
                normal: {
                  show: true,
                }
              },
              edgeSymbol: ['none', 'arrow'],
              edgeSymbolSize: [4, 8],//箭头大小
              edgeLabel: {
                normal: {
                  textStyle: {
                    fontSize: 20
                  }
                }
              },
              force: {
                repulsion: 1000, //斥力
                edgeLength: [30, 70] //默认距离
              },
              //            layout:'circular',
              draggable: true,
              lineStyle: {
                normal: {
                  width: 2,
                  color: '#4b565b',
                  curveness: 0,
                  length: 20
                }
              },
              edgeLabel: {
                normal: {
                  show: false,
                  formatter: function(x) {
                    return x.data.name;
                  }
                }
              },
              data: courseList,
              links: links
            }]
          }
          console.log("option:");
          console.log(option);
          this.option = option;
          this.showKg();
          console.log("go!:");
        })
        .catch(function(error){
          console.log(error);
      })
      // this.$store.dispatch('kg/getAllCourse')
      // .then(response=>{
      //   console.log(response);
      // })
    },
    showKg(){
      console.log("go!!!:");
      const kgShow = document.getElementById('kgShow');
      //获取div生成实例
      const myChart = echarts.init(kgShow,'light');
      myChart.setOption(this.option,{notMerge:true});
      let data = myChart._model.option.series[0].data;
      console.log("myChart._model");
      console.log(myChart._model);
      // data[0]:{name: '离散数学', symbolSize: 50, itemStyle:  }
      console.log("data");
      console.log(data);
      
      myChart.on("click",(chartParam)=>{
        // console.log(myChart._model);
        this.videoUploadPercent = 0;
        console.log("##########")
        console.log(chartParam);
        const entityName = data[chartParam.dataIndex]["name"];
        this.entityName = entityName;
        this.chartParam = chartParam;
        this.details();
        // this.initData();
      });
      
      myChart.on("mouseover",(chartParam)=>{
        console.log(chartParam);
      })
    },
    details(){
      this.drawer = true;
      this.drawerTitle = this.chartParam.name;
      var courseId = this.chartParam.dataIndex;
      var formData = new FormData();
      formData.append("courseName",this.drawerTitle);
      console.log(formData);
      var options = {
            url: 'http://localhost:8080/bj/courseInformation/getCourseByName',
            data:formData,
            method: 'post' ,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
          };
      this.$axios(options)
        .then(response =>{
          console.log("response");
          console.log(response);
          if(response.data.data!=null){
            
            var data = response.data.data;
            console.log(data.courseIntroduction);
            this.courseInformationForm.courseIntroduction = data.courseIntroduction;
            this.fullPptPath=data.coursePptpath;
            this.fullPracPath = data.coursePracticepath;
            this.fullVideoPath = data.courseVideopath;
            this.courseInformationForm.coursePptpath = this.getFileName(data.coursePptpath);
            this.courseInformationForm.coursePracticePath = this.getFileName(data.coursePracticepath);
            this.courseInformationForm.courseVideopath = this.getFileName(data.courseVideopath);

            // this.courseInformationForm.coursePptpath = data.coursePptpath;
            // this.courseInformationForm.coursePracticePath = data.coursePracticepath;
            // this.courseInformationForm.courseVideopath = data.courseVideopath;
            console.log("###")
            console.log(this.courseInformationForm.coursePptpath.length);
          }
        })
        .catch(function(error){
          console.log(error);
        })


    },
    handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            this.courseInformationForm.courseName = '';
            this.courseInformationForm.courseIntroduction = '';
            this.courseInformationForm.coursePptpath = '';
            this.courseInformationForm.coursePracticePath = '';
            this.courseInformationForm.courseVideopath = '';
            done();
          })
          .catch(_ => {});
    },
    changeAddStatus(item){
      console.log(item)
      if(item.value == 2){
        console.log("1111111");
        this.showStatus1 = true;
        // this.$set(this.showStatus1,true);
        // this.$forceUpdate();
      }else{
        this.showStatus1 = false;
        // this.$set(this.showStatus1,false);
        // this.$forceUpdate();

      }
      
    },
    add(){
      var addType = this.showStatus1;
      if(addType == true){
        // console.log("加入两个课程")
        //加子课程，要传入两个参数
        // var courseFrom = this.form.entityFrom;
        // var courseTo = this.form.entityTo;
        // console.log(courseFrom);
        //后端用的两个RequestParam就用formdata
        var formData = new FormData;
        formData.append("courseName1",this.form.entityFrom);
        formData.append("courseName2",this.form.entityTo);

        var options = {
            url: 'http://localhost:8080/bj/kg/addCourseAndBranchesByName',
            data:formData,
            method: 'post' ,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
          }
        this.$axios(options)
        .then(response =>{
          console.log("response");
          console.log(response);
          this.$message({
                message: '添加成功',
                type: 'success'
            })
          //加入节点之后要往mysql的课程信息表中也加入一个courseName; 此情况只需加入第二个节点即可
                var options1 = {
                  url: 'http://localhost:8080/bj/courseInformation/addCourseInformations',
                  data:{courseName:this.form.entityTo},
                  method: 'post' ,
                  // headers:{'Content-Type':'application/json;charset=UTF-8'},
                }
                this.$axios(options1)
                .then(response =>{
                  console.log("response");
                  console.log(response);
                  this.initData();
                })
                .catch(function(error){
                  console.log(error);
                })
          this.initData();
        })
        .catch(function(error){
          console.log(error);
        })
      }else{
        //只添加单个节点
        var options = {
            url: 'http://localhost:8080/bj/kg/addSingleNode',
            data:{name:this.form.entityFrom},
            method: 'post' ,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
          }
          this.$axios(options)
          .then(response =>{
            console.log("response");
            console.log(response);
            if(response.data.data.id!=null){
              //加入节点之后要往mysql的课程信息表中也加入一个courseName;
              var options2 = {
                  url: 'http://localhost:8080/bj/courseInformation/addCourseInformations',
                  data:{courseName:this.form.entityFrom},
                  method: 'post' ,
                  // headers:{'Content-Type':'application/json;charset=UTF-8'},
                }
                this.$axios(options2)
                .then(response =>{
                  console.log("response");
                  console.log(response);
                  this.$message({
                    message: '添加成功',
                    type: 'success'
                  })
                  this.initData();
                })
                .catch(function(error){
                  console.log(error);
                })
              
              this.initData();
            }else{
              this.$message({
                message: '请勿重复添加课程',
                type: 'error'
              })
            }
          
          })
          .catch(function(error){
            console.log(error);
          })
      }
    },
    // 视频上传前执行
    beforeUploadVideo (file) {
      const isLt300M = file.size / 1024 / 1024 < 300
      if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb','pdf'].indexOf(file.type) === -1) {
        this.$message.error('请上传正确的视频格式')
        return false
      }
      if (!isLt300M) {
        this.$message.error('上传视频大小不能超过300MB哦!')
        return false
      }
    },
    // 视频上传过程中执行
    uploadVideoProcess (event, file, fileList) {
      this.Plus = false
      this.videoFlag = true
      this.videoUploadPercent = file.percentage.toFixed(0)
    },
    // 视频上传成功是执行
    handleVideoSuccess (res, file) {
      this.Plus = false
      this.videoUploadPercent = 100
      console.log(res)
      // 如果为200代表视频保存成功
      if (res.resCode === '200') {
        // 接收视频传回来的名称和保存地址
        // 至于怎么使用看你啦~

        this.videoForm.videoId = res.newVidoeName
        this.videoForm.videoUrl = res.VideoUrl
        console.log("###########")
        console.log(this.videoForm.videoUrl);
        // this.getFileName(this.courseInformationForm.courseVideopath,this.videoForm.VideoUrl);
        this.courseInformationForm.courseVideopath = this.videoForm.VideoId;
        this.$message.success('视频上传成功！')
      } else {
        this.$message.error('视频上传失败，请重新上传！')
      }
    },
    handleExceedPractice(res, file){
      console.log("file###########");
      console.log(res);
      if (res.resCode === '200') {

        this.practiceForm.practiceId = res.newVidoeName
        this.practiceForm.practiceUrl = res.VideoUrl
        console.log("###########")
        console.log(this.practiceForm.practiceUrl);
        // this.getFileName(this.courseInformationForm.coursePracticePath,this.practiceForm.practiceUrl);
        this.courseInformationForm.coursePracticePath = this.practiceForm.practiceId;
        this.$message.success('文件上传成功！')
      } else {
        this.$message.error('文件上传失败，请重新上传！')
      }
    },
    handleExceedPpt(res, file){
      console.log("file###########");
      console.log(res);
      if (res.resCode === '200') {

        this.pptForm.pptId = res.newVidoeName
        this.pptForm.pptUrl = res.VideoUrl
        console.log("###########")
        console.log(this.pptForm.pptUrl);
        // this.getFileName(this.courseInformationForm.coursePptpath,this.pptForm.pptUrl);
        this.courseInformationForm.coursePptpath = this.pptForm.pptId;
        this.$message.success('文件上传成功！')
      } else {
        this.$message.error('文件上传失败，请重新上传！')
      }
    },
    updateIntroduction(){
      var introdution = this.courseInformationForm.courseIntroduction;
      var formData = new FormData();
      formData.append("courseName",this.entityName);
      formData.append("introduction",introdution);
      console.log("introduction1#######")
      console.log(this.courseInformationForm.courseIntroduction);
      var options = {
        url: 'http://localhost:8080/bj/courseInformation/updateIntroduction',
        data:formData,
        method: 'post' ,
        // headers:{'Content-Type':'application/json;charset=UTF-8'},
      }
      this.$axios(options)
          .then(response =>{
            console.log("introduction");
            console.log(response);
            this.$message.success('课程介绍上传成功！')
            // this.initData();
          })
          .catch(function(error){
            console.log(error);
      })
    }
    
  }
}
</script>

<style lang="scss" scoped>
.documentation-container {
  margin: 50px;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;

  .document-btn {
    flex-shrink: 0;
    display: block;
    cursor: pointer;
    background: black;
    color: white;
    height: 60px;
    padding: 0 16px;
    margin: 16px;
    line-height: 60px;
    font-size: 20px;
    text-align: center;
  }
}
</style>
<style>
  .kgContainer {
    width: 100%;
    background-color: #FFFFFF;
    padding: 32px;
  }
  
  .kgShow {
    width: 100%;
    height: 500px;
  }
</style>



