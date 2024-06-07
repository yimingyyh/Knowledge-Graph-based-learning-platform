<dynamic-table :tableHeader="tableHeader" :tableData="tableData" :config="config">
    <!-- 这里不得的赞叹插槽功能实在是太好用了，传递表格当前行的数据，插入自定义按钮，简直不要太爽-->
    <template v-slot:handles="t">
	  <el-button-group>
	    <el-link type="primary" @click="edit(t) icon="el-icon-edit">编辑</el-link>
	    <el-link type="danger" @click="delete(t)>删除<i class="el-icon-delete el-icon--right"></i> </el-link>
      </el-button-group>
    </template>
</dynamic-table>
<el-table
			class="customer"
			:data="tableData"
			style="width: 100%">
			<!-- 循环表头数据，判断列显示类型 -->
			<template v-for="(col,index) in tableHeader">
				<!-- 多选框 -->
				<el-table-column v-if="col.type == 'selection'" :label="col.label" :type="col.type" :width="col.width"></el-table-column>
				<!-- 索引行 -->
				<el-table-column v-else-if="col.type == 'index'" :label="col.label" :type="col.type" :width="col.width"></el-table-column>
                <!-- 图片 -->
				<el-table-column v-else-if="col.type == 'image'" :label="col.label" :width="col.width" :prop="col.prop">
					<template slot-scope="scope">
						<div class="content-image"><img :src="scope.row[col.prop]"/></div>
					</template>
				</el-table-column>
                <!-- 给日期行加个好看的icon，属于自定义，所以要单独抽离出来 -->
				<el-table-column v-else-if="col.type == 'date'" :label="col.label" :width="col.width" :min-prop="col.prop">
					<template slot-scope="scope">
						<i class="el-icon-time"></i>
						<span style="margin-left: 10px">{{ scope.row[col.prop] }}</span>
					</template>
				</el-table-column>
                <!-- 操作按钮，我们不知道表格操作按钮业务需求是什么样子的，那我们就给插槽，不同业务可以按需求添加操作按钮 -->
				<el-table-column v-else-if="col.type == 'handle'" :label="col.label" :min-width="col.width" :fixed="col.fixed">
					<template slot-scope="scope">
						<slot name="handles" :col="scope.row"></slot>
					</template>
				</el-table-column>
                <!-- 对于其他没有类型的列，统统算作普通默认类型。直接按照表格属性呈现就行 -->
				<el-table-column v-else :label="col.label" :min-width="col.width" :prop="col.prop" :formatter="col.formatter?col.formatter:null">
				</el-table-column>
			</template>
		</el-table>