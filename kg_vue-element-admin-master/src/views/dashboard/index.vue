<template>
    <div id="app">
      <section class="section">
        <h4>
          待办
        </h4>
      </section>
      <Kanban :stages="statuses" :blocks="blocks" @update-block="updateBlock">
        <div v-for="stage in statuses" :slot="stage" :key="stage">
          <h2>
            {{ stage }}
            <!-- <a>+</a> -->
          </h2>
        </div>
        <!-- 待办事件本体 -->
        <div v-for="item in blocks" :slot="item.id" :key="item.id">
          <div>
            {{ item.msg }}
          </div>
<!-- 全部挪到finished 然后finished会单独有一个删除 可以删除全部 -->
        </div>
        <!-- 添加新的 -->
        <div v-for="stage in statuses" :key="stage" :slot="`footer-${stage}`">
            <a href="" @click.prevent="() => addBlock(stage)">+ Add new block  </a>
            <span v-html="'\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0'"></span>
            <el-link type="danger" @click="deleteFinishedMsg()" v-if="stage=='finished'">删除<i class="el-icon-delete el-icon--right"></i> </el-link>
        </div>
      </Kanban>

      <el-dialog class="addMsg" :visible.sync="addDialogFormVisible">
      <el-form :model="addTemp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="待办事件" prop="msg">
          <el-input v-model="addTemp.addMsgTemp" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="addMsg()">
          添加待办
        </el-button>
      </div>
    </el-dialog>
    </div>
  </template>
  
  <script>
  import faker from 'faker';
  import { debounce } from 'lodash';
  import Kanban from '@/components/Kanban';
import { method } from 'lodash';
  
  export default {
    name: 'app',
    components: {
      Kanban,
    },
    data() {
      return {
        nowStatus:'',
        addTemp:{
            addMsgTemp:'',
        },
        addDialogFormVisible:false,
        userId: localStorage.getItem("userId"),
        userType: localStorage.getItem("userType"),
        statuses: ['waiting',  'doing', 'finished'],
        // statuses: ['on-hold', 'in-progress', 'needs-review', 'approved'],
        blocks: [],
      };
    },
    mounted() {
        console.log("uid+utype");
        console.log(this.userId);
        this.initData();
    //   for (let i = 0; i <= 10; i += 1) {
    //     this.blocks.push({
    //       id: i,
    //       status: this.statuses[Math.floor(Math.random() * 4)],
    //       title: faker.company.bs(),
    //     });
    //   }
    },
  
    methods: {
        deleteFinishedMsg(){
          var options={
                url: "http://localhost:8080/bj/kanban/deleteFinished",
                method:'post',
            }
          this.$axios(options)
            .then(response=>{
              console.log("deleteFinished")
              console.log(response);
              this.$message({
                  message: '移除成功',
                  type: 'success'
              })
              this.initData();
            })
        },
        addMsg(){
            var formData = new FormData();
            formData.append("userId",this.userId);
            formData.append("msg",this.addTemp.addMsgTemp);
            formData.append("status",this.nowStatus);
            var options={
                url: "http://localhost:8080/bj/kanban/addMsg",
                method:'post',
                data:formData,
            }
            this.$axios(options)
                .then(response=>{
                    console.log("addMsg");
                    console.log(response);
                    if(this.addTemp.addMsgTemp!=''){
                        console.log("message")
                        this.blocks.push({
                            id: Math.random(),
                            status: this.nowStatus,
                            msg: this.addTemp.addMsgTemp,
                        });
                        console.log(this.blocks)
                        this.$message({
                            message: '添加成功',
                            type: 'success'
                        })
                        this.initData();
                    }


                })
                .catch(function(error){
                    console.log(error);
                })
        },
        initData(){
            var formData = new FormData();
            formData.append("userId",this.userId);
            var options = {
                url:"http://localhost:8080/bj/kanban/getListByUser",
                method:'post',
                data:formData
            }
            this.$axios(options)
                .then(response=>{
                    console.log("getListByUser")
                    console.log(response);
                    console.log(response.data.data[1]["msg"])
                    // {msgId: 1, userId: '123456', msg: '写作业', status: 'doing', deleted: 0}
                    var list = response.data.data;
                    this.blocks = [];
                    this.addMsgTemp='';
                    for(var i = 0;i<list.length;i++){
                        if(list[i]["msg"]!=''){
                            this.blocks.push({
                            id:  list[i]["msgId"],
                            status: list[i]["status"],
                            msg: list[i]["msg"],
                        })
                        }
                        
                    }
                })
                .catch(function(error){
                    console.log(error);
                })
        },
        updateBlock: debounce(function (id, status) {
            var data = new FormData();
            data.append("msgId",id);
            data.append("status",status);
            var options = {
              url:"http://localhost:8080/bj/kanban/changeStatus",
              method:'post',
              data:data,
            }
            this.$axios(options)
              .then(response=>{
                console.log("changeStatus");
                console.log(response);
              })
              .catch(function(error){
                console.log(error);
              })
            // this.blocks.find(b => b.id === Number(id)).status = status;
        }, 500),
        addBlock: debounce( function (stage) {
            //出现弹窗 输入相应信息之后再加入
            this.nowStatus = stage;
            this.addDialogFormVisible = true;
            // this.addMsg();

        }, 500),
    },
  };
  </script>
  
  <style lang="scss">
    @import './kanban.scss';
  
    $waiting: #FB7D44;
    $doing: #2A92BF;
    // $needs-review: #F4CE46;
    $finished: #00B961;
  
    * {
      box-sizing: border-box;
    }
  
    body {
      background: #ffffff;
      color: white;
      font-family: 'Lato';
      font-weight: 300;
      line-height: 1.5;
      -webkit-font-smoothing: antialiased;
    }
  
    .drag-column {
      .drag-column-header > div {
        width: 100%;
        h2 > a {
          float: right;
        }
      }
  
      .drag-column-footer > div {
          margin-left: 10px;
          a {
              text-decoration: none;
              color: white;
              &:hover {
                  text-decoration: underline;
              }
          }
      }
  
      &-waiting {
        .drag-column-header,
        .is-moved,
        .drag-options {
          background: $waiting;
        }
      }
  
      &-doing {
        .drag-column-header,
        .is-moved,
        .drag-options {
          background: $doing;
        }
      }
  
    //   &-needs-review {
    //     .drag-column-header,
    //     .is-moved,
    //     .drag-options{
    //       background: $needs-review;
    //     }
    //   }
  
      &-finished {
        .drag-column-header,
        .is-moved,
        .drag-options {
          background: $finished;
        }
      }
    }
  
    .section {
      padding: 20px;
      text-align: center;
  
      a {
        color: white;
        text-decoration: none;
        font-weight: 300;
      }
  
      h4 {
        font-weight: 400;
        a {
          font-weight: 600;
        }
      }
    }
  </style>
  