<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="search" placeholder="知识点名称" style="width: 300px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <!-- <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select> -->
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="findTest"  >
        搜索
      </el-button>
      <!-- <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        导出
      </el-button> -->
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="120px" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row[0] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="课程名" prop="name"  align="center" width="220px" >
        <template slot-scope="{row}">
          <span>{{ row[1] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="课程介绍" prop="introduction"  align="center" min-width="600px" >
        <template slot-scope="{row}">
          <span>{{ row[2] }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="350" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="showTest(row)" v-if="userType == 1" >
            查看测试
          </el-button>
          <el-button type="primary" size="mini" @click="stuRes(row)" v-if="userType == 1" >
            测试情况
          </el-button>
          <el-button type="primary" size="mini"  @click="doTest(row)" v-else >
            进行测试
          </el-button>
          <el-button  size="mini" type="success" @click="getRes(row)" v-if="userType == 2" >
            测试结果
          </el-button>
          <el-button  size="mini" type="success" @click="addTest(row)" v-if="userType == 1" >
            添加测试
          </el-button>
          <span v-html="'\u00a0\u00a0\u00a0'"></span>
          <el-button  size="mini" type="danger" @click="deleteAllTests(row)" v-if="userType == 1">
            删除测试
          </el-button>
          
        </template>
      </el-table-column>
    </el-table>
    <div class="page" >
      <el-pagination
      v-show="totalNum>0"
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[5,10,20,50,100]"
      :page-size="pageSize"
      :total="totalNum"
      layout="sizes,prev,pager,next">
      </el-pagination>
      <span>总共{{ totalNum }}条</span>
    </div>
    <!-- 学生端看成绩 -->
    
    <!-- <el-dialog  class="showD" v-el-drag-dialog :visible.sync="showDialogFormVisible" :title="showTitle" @dragDialog="handleDrag"> -->
    <el-dialog
      title="测试成绩"
      class="showRes"
      v-el-drag-dialog
      :visible.sync="resDialogVisible"
      @dragDialog="handleDrag"
      custom-class="selfDialog"
      >
      <h3><span v-html="'\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0'"></span>{{stuResName}}的测试结果是{{resScore}}分</h3>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="resDialogVisible = false">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 学生成绩的dialog -->
    <el-dialog
      title="学生测试成绩分布"
      :visible.sync="chartDialogVisible"
      width="50%"
      @opened="fetchScores"
    >
      <div ref="chart" style="width: 100%; height: 400px;"></div>
    </el-dialog>

    <!-- 学生作答的dialog -->
    <el-dialog  class="doTest" v-el-drag-dialog :visible.sync="doTestDialogFormVisible"  @dragDialog="handleDrag">
      <h3>一、单选题</h3>
      <div v-for="(item, index) in testTemp.single" :key="index" class="single">
          <p>{{index+1}}、{{ item[2] }}</p>
          <el-radio-group v-model="answers.single[index].answer">
            <el-radio label="A">
              <span>A.<span v-html="'\u00a0\u00a0'"></span>{{item[3]}}</span>
            </el-radio>
            <el-radio label="B">
              <span>B.<span v-html="'\u00a0\u00a0'"></span>{{item[4]}}</span>
            </el-radio>
            <el-radio label="C">
              <span>C.<span v-html="'\u00a0\u00a0'"></span>{{item[5]}}</span>
            </el-radio>
            <el-radio label="D">
              <span>D.<span v-html="'\u00a0\u00a0'"></span>{{item[6]}}</span>
            </el-radio>
          </el-radio-group>
      </div>
      <h3>二、多选题</h3>
      <div v-for="(item, index) in testTemp.mul" :key="index" class="mul">
        <p>{{index+1}}、{{ item[2] }}</p>
          <el-checkbox-group v-model="answers.mul[index].answer">
            <el-checkbox label="A">
              <span>A.<span v-html="'\u00a0\u00a0'"></span>{{item[3]}}</span>
            </el-checkbox>
            <el-checkbox label="B">
              <span>B.<span v-html="'\u00a0\u00a0'"></span>{{item[4]}}</span>
            </el-checkbox>
            <el-checkbox label="C">
              <span>C.<span v-html="'\u00a0\u00a0'"></span>{{item[5]}}</span>
            </el-checkbox>
            <el-checkbox label="D">
              <span>D.<span v-html="'\u00a0\u00a0'"></span>{{item[6]}}</span>
            </el-checkbox>
          </el-checkbox-group>
      </div>
      <h3>三、问答题</h3>
      <div v-for="(item, index) in testTemp.block" :key="index" class="block">
        <p>{{index+1}}、{{ item[2] }}</p>
          <el-input  v-model="answers.block[index].answer" type="textarea"  maxlength="200" show-word-limit autocomplete="off"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="doTestDialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary"  @click="submitTest()">
          提交试题
        </el-button>
        <!-- :disabled="!isFormValid" -->
      </div>
    </el-dialog>


<!-- show的dialogForm -->
    <el-dialog  class="showD" v-el-drag-dialog :visible.sync="showDialogFormVisible" :title="showTitle" @dragDialog="handleDrag">
      <el-table :data="showList">
        <el-table-column property="id" label="序号" width="100" >
          <template slot-scope="{row}">
            <span>{{ row[0] }}</span>
          </template>
        </el-table-column>
        <el-table-column property="testName" label="题目" width="200" >
          <template slot-scope="{row}">
            <span>{{ row[3] }}</span>
          </template>
        </el-table-column>
        <el-table-column property="address" label="题目类型" >
          <template slot-scope="{row}">
            <span>{{ showTestType[row[2]] }}</span>
          </template>
        </el-table-column>
        <el-table-column property="operator" label="操作" >
          <template slot-scope="{row,$index}">
            <el-button-group>
              <!-- <el-link type="primary" @click="edit(row)" icon="el-icon-edit">编辑</el-link> -->
              <!-- 根据row[1]删除 row[1]是test在数据库中的id -->
              <el-link type="danger" @click="deleteShowTestItem(row,$index)">删除<i class="el-icon-delete el-icon--right"></i> </el-link>
                    </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-show="totalNumShow>0"
        background
        @size-change="handleSizeChangeShow"
        @current-change="handleCurrentChangeShow"
        :current-page="currentPageShow"
        :page-sizes="[5,10,20,50,100]"
        :page-size="pageSizeShow"
        :total="totalNumShow"
        layout="sizes,prev,pager,next">
      </el-pagination>
      <span>总共{{ totalNumShow }}条</span>
    </el-dialog>

    <!-- add的dialogform -->
    <el-dialog class="addD" :visible.sync="addDialogFormVisible">
      <el-form :model="addTemp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="课程名字" prop="courseName">
          <el-input v-model="addTemp.courseName"  :disabled="true"/>
        </el-form-item>
        <el-form-item label="题目" prop="testName" >
          <el-input v-model="addTemp.testName" type="textarea"  maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="题目类型" prop="testType">
          <!-- <span>test</span> -->
          <el-select v-model="addTemp.type">
            <el-option
              v-for="item in elOptons"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              @click.native="changeTestType(item)">
            </el-option>
          </el-select>
          <el-radio-group v-model="addTemp.single" v-if="testTypeStatus.single">
            <el-radio label="A">
              <span>A<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.a"></el-input>
            </el-radio>
            <el-radio label="B">
              <span>B<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.b"></el-input>
            </el-radio>
            <el-radio label="C">
              <span>C<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.c"></el-input>
            </el-radio>
            <el-radio label="D">
              <span>D<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.d"></el-input>
            </el-radio>
          </el-radio-group>

          <el-checkbox-group v-model="checkList" v-if="testTypeStatus.mul">
            <el-checkbox label="A">
              <span>A<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.a"></el-input>
            </el-checkbox>
            <el-checkbox label="B">
              <span>B<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.b"></el-input>
            </el-checkbox>
            <el-checkbox label="C">
              <span>C<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.c"></el-input>
            </el-checkbox>
            <el-checkbox label="D">
              <span>D<span v-html="'\u00a0\u00a0'"></span></span><el-input v-model="addTemp.d"></el-input>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" :disabled="!isFormValid" @click="addTests()">
          提交试题
        </el-button>
      </div>
    </el-dialog>


    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>


<script>
import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import elDragDialog from '@/directive/el-drag-dialog' // base on element-ui
import * as echarts from 'echarts';
import { first } from 'lodash'
import { Form } from 'element-ui'
const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'DragDialogDemo',
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves ,elDragDialog},
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      search:"",
      resName:"",
      stuResName:"",
      resScore:0,
      resDialogVisible: false,
      chartDialogVisible: false,
      chartInstance: null,
      answers: {
        single: [{
          
        }],
        mul: [],
        block: []
      },
      testTypeStatus:{
        single:false,
        mul:false
      },
      testList:[],
      testTemp:{
        single:[],
        mul:[],
        block:[],
      },
      testTempItem:{
        id: undefined,
        courseName:'',
        testName:'',
        type: '',
        single:'',
        checkList:[],
        a:'',
        b:'',
        c:'',
        d:'',
        block:'',
        selectAns:'',
      },
      userId: localStorage.getItem("userId"),
      userType: localStorage.getItem("userType"),
      userName: localStorage.getItem("userName"),
      currentPageShow:1,
      pageSizeShow:5,
      totalNumShow:0,//totalNum处理不好会导致每次查询只显示一页
      sortedTestList:[],
      showList:[],
      showTitle:'',
      checkList: [],
      test:'',
      showTestType:{
        1:'单选题',
        2:'多选题',
        3:'问答题'
      },
      doTypeStatus:{
        single:false,
        mul:false,
        block:false,
      },
      elOptons:[{
        value:1,
        label:'单选题'
        },{
          value:2,
          label:'多选题'
        },
        {
          value:3,
          label:'填空题'
        }
      ],
      addTemp: {
        id: undefined,
        courseName:'',
        testName:'',
        type: '',
        single:'',
        checkList:[],
        a:'',
        b:'',
        c:'',
        d:'',
        block:'',
        selectAns:'',
      },
      currentPage:1,
      pageSize:20,
      totalNum:0,//totalNum处理不好会导致每次查询只显示一页
      showPage:true,
      allTableData:[],
      // pageSize:10,
      // pageSizes: [5, 10,20,50],
      tableKey: 0,
      list: null,
      // total: 0,
      listLoading: true,
      // pageList:{
      //   currentPage:1,
      //   pageSize:20,
      //   totalNum:0
      // },
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        type: '',
        status: 'published'
      },
      doTestDialogFormVisible:false,
      showDialogFormVisible: false,
      addDialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  watch: {
    'testTemp.single': {
      handler(newValue, oldValue) {
        this.answers.single = newValue.map(item => ({ testId: item[0], answer: null ,type: 1, userId: this.userId }));
      },
      immediate: true, // 立即执行，确保初始数据也处理
      deep: true
    },
    'testTemp.mul': {
      handler(newValue, oldValue) {
        this.answers.mul = newValue.map(item => ({ testId: item[0], answer: [], type: 2, userId: this.userId }));
      },
      immediate: true,
      deep: true
    },
    'testTemp.block': {
      handler(newValue, oldValue) {
        this.answers.block = newValue.map(item => ({ testId: item[0], answer: '' , type: 3, userId: this.userId }));
      },
      immediate: true,
      deep: true
    }
  },
  computed: {
  // 计算属性用于检查所有答案是否已完成
    isFormValid() {
      // 检查单选题是否每题都有答案
      const allSinglesAnswered = this.answers.single.every(answer => answer.answer !== undefined);
      // 检查多选题是否每题都至少选择了一个选项
      const allMultiplesAnswered = this.answers.mul.every(answer => answer.answer.length > 0);
      // 检查问答题是否每题都已填写
      const allBlocksAnswered = this.answers.block.every(answer => answer.answer.trim() !== '');

      // 只有所有类型的题目都正确回答了，表单才算有效
      return allSinglesAnswered && allMultiplesAnswered && allBlocksAnswered;
    }
  },
  created() {
    this.getNumOfCourse();
    this.getList(this.currentPage,this.pageSize);
    // this.testTemp.single.forEach((item, index) => {
    //     this.answers.single[index] = { testId: item[1], answer: null };
    //   });
    //   this.testTemp.mul.forEach((item, index) => {
    //     this.answers.mul[index] = { testId: item[1], answer: [] };
    //   });
    //   this.testTemp.block.forEach((item, index) => {
    //     this.answers.block[index] = { testId: item[1], answer: '' };
    //   });
    // this.getList(1,20);
  },
  methods: {
    findTest(){
      var courseName = this.search;
      console.log(courseName)
      var formData = new FormData();
      formData.append("courseName",courseName);
      var options={
        url:'http://localhost:8080/bj/courseTest/findByCourseName',
        method:'post',
        data:formData,
      }
      this.$axios(options)
          .then(response=>{
            console.log("findByCourseName#####")
            console.log(response);
            var ans = response.data.list;
            ans[0].unshift(1);
            console.log("changeNewList#####");
            this.pageSize = 10;
            this.currentPage = 1;
            this.total = 1;
            this.list = ans;
            console.log(this.list);
            this.listLoading = false;
          })
          .catch(function(error){
            console.log(error);
            this.$message({
                  message: '知识点名称有误',
                  type: 'danger'
                })
          })
      
    },
    getRes(row){
      this.stuResName = row[1];
      var courseName = row[1];
      var formData = new FormData();
      formData.append("courseName",courseName);
      formData.append("userId",this.userId);
      var options={
        url:'http://localhost:8080/bj/doTest/getScoreByName',
        method:'post',
        data:formData,
      }
      this.$axios(options)
          .then(response=>{
            console.log("showRes######");
            console.log(response);
            var score = response.data.data;
            this.resScore = score;
            this.resDialogVisible = true;
          })
          .catch(function(error){
            console.log(error);
          })
    },
    stuRes(row){
      this.resName = row[1];
      this.chartDialogVisible = true;
    },
    initChart(data) {
      if (this.chartInstance === null) {
        this.chartInstance = echarts.init(this.$refs.chart);
      }
      this.chartInstance.setOption({
        title: {
          text: this.resName,
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '成绩区间',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      });
    },
    fetchScores() {
      var formData = new FormData();
      formData.append("courseName",this.resName);
      console.log("preparing....")
      var options = {
        url:"http://localhost:8080/bj/courseTest/getStuRes",
        method:"post",
        data:formData
      }
      this.$axios(options)
        .then(response => {
          console.log("stuRes###")
          console.log(response)
          const aggregatedData = this.aggregateScores(response.data.data);
          this.initChart(aggregatedData);
        })
        .catch(error => {
          console.error('获取数据失败:', error);
        });
    },
    aggregateScores(scores) {
      // userId,userName,total [1,2,3]
      let intervals = {
        '90-100分': 0,
        '80-89分': 0,
        '70-79分': 0,
        '60-69分': 0,
        '<60分': 0
      };
      scores.forEach(score => {
        if (score[2] >= 90) intervals['90-100分']++;
        else if (score[2] >= 80) intervals['80-89分']++;
        else if (score[2] >= 70) intervals['70-79分']++;
        else if (score[2] >= 60) intervals['60-69分']++;
        else intervals['<60分']++;
      });

      return Object.keys(intervals).map(key => ({
        name: key,
        value: intervals[key]
      }));
    },
    beforeDestroy() {
    if (this.chartInstance !== null) {
        this.chartInstance.dispose();
      }
    },
    submitTest(){
      if (this.isFormValid) {
        // 提交逻辑
        console.log("submitTest####")
        console.log(this.answers.single);
        console.log(this.answers.mul);
        console.log(this.answers.block);
        // [0]:  anwser:  testId:  type:  userId:  
        // ({ testId: item[0], answer: null ,type: 1, userId: this.userId })
        const results = [];
                // 收集单选题答案
        if(this.answers.single.length>0){
          this.answers.single.forEach((item, index) => {
          results.push({
            // type: item.type,
            testId: parseInt(item.testId,10),
            selectAns: item.answer,
            userId: this.userId,
            blockAns:'',
            score:0.0,
            isFinished:1
          });
        });
        }
        

        // 收集多选题答案
        if(this.answers.mul.length>0){
          this.answers.mul.forEach((item, index) => {
          var list = item.answer;
          var ans = '';
          for(var i = 0;i<list.length;i++)
            ans+=list[i];
          console.log("ans");
          // console.log(item);
          console.log(ans);
          results.push({
            // type: item.type,
              testId: parseInt(item.testId,10),
              selectAns: ans,
              userId: this.userId,
              blockAns:'',
              score:0.0,
              isFinished:1
            });
          });
        }
        

        // 收集问答题答案
        if(this.answers.block.length>0){
          this.answers.block.forEach((item, index) => {
          results.push({
            // type: item.type,
              testId: parseInt(item.testId,10),
              blockAns: item.answer,
              userId: this.userId,
              selectAns:'',
              score:0.0,
              isFinished:1
            });
          });
        }
        
        console.log("results#######")
        console.log(results);

        var options = {
          url: "http://localhost:8080/bj/doTest/submitTest",
          method:'post',
          data: results,
        }
        this.$axios(options)
            .then(response=>{
              console.log("submitResponse");
              console.log(response);
              if(response.data === "Tests processed successfully!"){
                this.$message({
                  message: '提交成功',
                  type: 'success'
                })
              }
              this.doTestDialogFormVisible = false;
            })
            .catch(function(error){
              console.log(error);
            })
      } else {
        // alert('请完成所有题目后再提交！');
        this.$message({
          message: '请完成所有题目后再提交!',
          type: 'error'
        })

      }
    },
    doTest(row){
      this.doTestDialogFormVisible = true;
      this.testTemp.single = [];
      this.testTemp.mul = [];
      this.testTemp.block = [];
       // 初始化答案模型
      // this.testTemp.single.forEach((item, index) => {
      //   this.answers.single[index] = { testId: item[1], answer: null };
      // });
      // this.testTemp.mul.forEach((item, index) => {
      //   this.answers.mul[index] = { testId: item[1], answer: [] };
      // });
      // this.testTemp.block.forEach((item, index) => {
      //   this.answers.block[index] = { testId: item[1], answer: '' };
      // });
      var courseName = row[1];
      var api = "http://localhost:8080/bj/courseTest/showTestByName/"+courseName+"/"+1+'/'+99999999;
      console.log("doTestrequest###")
      // console.log(api);
      var options = {
            url: api,
            method: 'get' ,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
      }
      this.$axios(options)
          .then(response =>{
            var lists = response.data.data.list;
            this.testList = lists;
            console.log("testList##########")
            console.log(lists);
            for(var i = 0; i< lists.length; i++){
              if(lists[i][1] == 1){
                //单选
                this.testTemp.single.push(lists[i]);
              }else if(lists[i][1] == 2){
                //多选
                this.testTemp.mul.push(lists[i]);
              }else{
                //问答
                this.testTemp.block.push(lists[i]);
              }
            }
            console.log("single&&mul&&block--list");
            console.log(this.testTemp.single);
            console.log(this.testTemp.mul);
            console.log(this.testTemp.block);
          })
          .catch(function(error){
            console.log(error);
          })
    },
    deleteAllTests(row){
      var name = row[1];
      var api = "http://localhost:8080/bj/courseTest/deleteTestsByName";
      var formData = new FormData();
      formData.append("courseName",name);
      var options={
        url:api,
        method:'post',
        data:formData,
      }
      this.$axios(options)
          .then(response=>{
            console.log("deleteTestResposne");
            console.log(response);
            this.showList = [];
            this.totalNumShow = 0;
            // this.$message({
            //     message: '删除成功',
            //     type: 'success'
            // })
          })
          .catch(function(error){
            console.log(error);
          })

    },
    deleteShowTestItem(row,index){
      var id = row[1];
      var api = "http://localhost:8080/bj/courseTest/deleteById";
      var formData = new FormData();
      formData.append("testId",id);
      console.log("request###api")
      console.log(api);
      var options = {
            url: api,
            method: 'post' ,
            data: formData,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
      }
      this.$axios(options)
          .then(response =>{
            console.log("responseDeleteItem");
            console.log(response);
            // console.log(this.showList);
            this.showList.splice(index, 1)
            this.getShowList(row[3],this.currentPageShow,this.pageSizeShow);
            this.$message({
                message: '删除成功',
                type: 'success'
            })
          })
          .catch(function(error){
            console.log(error);
          })
    },
    handleSizeChangeShow:function(size){
      console.log("size#####")
      this.pageSizeShow = size;
      this.getShowList(this.showTitle,this.currentPageShow,this.pageSizeShow);
      // if (this.autoScroll) {
      //   scrollTo(0, 800)
      // }
    },
    handleCurrentChangeShow:function(current){
      console.log("currentPage#####")
      this.currentPageShow = current;
      this.getShowList(this.showTitle,this.currentPageShow,this.pageSizeShow);
      // if (this.autoScroll) {
      //   scrollTo(0, 800)
      // }
    },
    getNumOfCourseShow(courseName){
      var api = "http://localhost:8080/bj/courseTest/showTestByName/"+courseName+"/"+1+'/'+99999999;
      console.log("request###api")
      console.log(api);
      var options = {
            url: api,
            method: 'get' ,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
      }
      this.$axios(options)
          .then(response =>{
            console.log("response");
            console.log(response);
            var tempList = response.data.data.list;
            this.totalNumShow = tempList.length;
          })
          .catch(function(error){
            console.log(error);
          })
    },
    handleDrag() {
      this.$refs.select.blur()
    },
    getShowList(courseName,currentPageShow,pageSizeShow){
      var api = "http://localhost:8080/bj/courseTest/showTestByName/"+courseName+"/"+currentPageShow+"/"+pageSizeShow;
      var options = {
            url: api,
            method: 'get' ,
            // block:this.addTemp.block,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
      }
      this.$axios(options)
          .then(response =>{
            console.log("responseShow");
            console.log(response);
            var list = response.data.data.list;
            if(list.length>0){
              console.log("list");
              console.log(list);
              for(let i = 0 ; i< list.length ;i++){
              // tempList[i].unshift(i+1);
                list[i].unshift(this.pageSizeShow*(this.currentPageShow-1)+1+i);
              }
              console.log("change");
              console.log(list);
              this.getNumOfCourseShow(courseName);
              this.showList = list;
              // this.totalNumShow = list.length;
              this.$message({
                message: '查询成功',
                type: 'success'
              })

            }else{
              // this.$message({
              //   message: '没有相应数据',
              //   type: 'error'
              // })
            }
          })
          .catch(function(error){
            console.log(error);
          })
    },
    showTest(row){
      this.showTitle = row[1];
      this.showDialogFormVisible = true;
      var courseName = row[1];
      this.getShowList(courseName,this.currentPageShow,this.pageSizeShow);
    },
    addTests(){
      var api = "http://localhost:8080/bj/courseTest/addTest";
      console.log("request###api1111111")
      console.log(api);
      var ans='';
      var list = this.checkList;
      if(this.addTemp.type == 1){
        ans = this.addTemp.single;
      }else if(this.addTemp.type == 2){
        //修改成一个字符串
        for(var i = 0;i<list.length;i++){
          ans+=list[i];
        }
        console.log("ans&list");
        console.log(list);
        console.log(ans);
      }else{
        ans = '';
      }
      console.log(this.addTemp.type);//1 2 3
      var options = {
            url: api,
            method: 'post' ,
            data:{courseName:this.addTemp.courseName,
                  testType:this.addTemp.type,
                  testName:this.addTemp.testName,
                  a:this.addTemp.a,
                  b:this.addTemp.b,
                  c:this.addTemp.c,
                  d:this.addTemp.d,
                  selectAns:ans,
                  },
            // block:this.addTemp.block,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
      }
      this.$axios(options)
          .then(response =>{
            console.log("responseTest");
            console.log(response);
            if(response.data.msg=="操作成功!"){
              this.$message({
                message: '添加成功',
                type: 'success'
              })
            }else{
              this.$message({
                message: '添加失败',
                type: 'error'
              })
            }
          })
          .catch(function(error){
            console.log(error);
          })
    },
    changeTestType(item){
      console.log("item#######");
      // console.log(item);
      // this.addTemp.a='';
      // this.addTemp.b='';
      // this.addTemp.c='';
      // this.addTemp.d='';
      if(item.value == 1){

        // this.addTemp.a='';
        // this.addTemp.b='';
        // this.addTemp.c='';
        // this.addTemp.d='';
        this.testTypeStatus.single = true;
        this.testTypeStatus.mul = false;
        
        // this.testTypeStatus.block = false;
      }
      if(item.value == 2){
        console.log(this.testTypeStatus.mul);
        this.testTypeStatus.single = false;
        this.testTypeStatus.mul = true;
        console.log(this.testTypeStatus.mul)
        // this.addTemp.a='';
        // this.addTemp.b='';
        // this.addTemp.c='';
        // this.addTemp.d='';
        // this.$forceUpdate();
        // this.testTypeStatus.block = false;
      }
      if(item.value == 3){
        this.testTypeStatus.single = false;
        this.testTypeStatus.mul = false;
        // this.testTypeStatus.block = true;
      }
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.showDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    addTest(row){
      //初始化一下
      this.testTypeStatus.single = false;
      this.testTypeStatus.mul = false;
      // this.testTypeStatus.block = false;
      this.addTemp = Object.assign({}, row) // copy obj
      this.addTemp.courseName = row[1];
      this.addDialogFormVisible = true;
      console.log(this.addTemp);
    },
    getNumOfCourse(){
      var api = "http://localhost:8080/bj/courseTest/getCourseList/"+1+'/'+99999999;
      console.log("request###api")
      console.log(api);
      var options = {
            url: api,
            method: 'get' ,
            // headers:{'Content-Type':'application/json;charset=UTF-8'},
      }
      this.$axios(options)
          .then(response =>{
            console.log("response");
            console.log(response);
            var tempList = response.data.list;
            this.totalNum = tempList.length;
          })
          .catch(function(error){
            console.log(error);
          })
    },
    handleSizeChange:function(size){
      console.log("size#####")
      this.pageSize = size;
      this.getList(this.currentPage,this.pageSize);
      // if (this.autoScroll) {
      //   scrollTo(0, 800)
      // }
    },
    handleCurrentChange:function(current){
      console.log("currentPage#####")
      this.currentPage = current;
      this.getList(this.currentPage,this.pageSize);
      // if (this.autoScroll) {
      //   scrollTo(0, 800)
      // }
    },
    getList(currentPage,size) {
      this.listLoading = true;
      var api = "http://localhost:8080/bj/courseTest/getCourseList/"+currentPage+'/'+size;
      console.log("request###api")
      console.log(api);
      var options = {
            url: api,
            method: 'get' ,
      }
      this.$axios(options)
          .then(response =>{
            console.log("responseList");
            console.log(response);
            var tempList = response.data.list;
            for(let i = 0 ; i< tempList.length ;i++){
              tempList[i].unshift(this.pageSize*(this.currentPage-1)+1+i);
            }
            console.log("change");
            this.list = response.data.list;
            console.log(this.list);
            this.listLoading = false;
          })
          .catch(function(error){
            console.log(error);
          })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.showDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          createArticle(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.showDialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.showDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateArticle(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.showDialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$notify({
        title: 'Success',
        message: 'Delete Successfully',
        type: 'success',
        duration: 2000
      })
      this.list.splice(index, 1)
    },
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },

  }
}
</script>


<style lang="scss">
  .selfDialog{
    height: auto;
    width: 30%;
  }
</style>