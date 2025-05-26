<template>
    <div>
<!--        <el-card>-->
<!--            <el-text style="font-size: 18px">-->
<!--                视频的保存时间为：-->
<!--            </el-text>-->
<!--            <el-input-number style="align-content: center" v-model="videotime" @change="changevideotime" :min="1"-->
<!--                             :max="10"-->
<!--                             label="视频保存时间"></el-input-number>-->
<!--            （单位：天）-->
<!--        </el-card>-->
        <div style="margin: 10px 0">
            <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                      v-model="name"></el-input>
            <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div style="margin: 10px 0">
            <el-popconfirm
                    class="ml-5"
                    confirm-button-text='确定'
                    cancel-button-text='我再想想'
                    icon="el-icon-info"
                    icon-color="red"
                    title="您确定批量删除这些数据吗？"
                    @confirm="delBatch"
            >
                <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
            </el-popconfirm>

        </div>
        <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"></el-table-column>
            <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
            <el-table-column prop="name" label="视频名称" align="center"></el-table-column>
            <el-table-column prop="createtime" label="视频产生时间" align="center"></el-table-column>
            <!--            <el-table-column prop="url" label="视频链接" align="center"></el-table-column>-->
            <el-table-column prop="size" label="视频大小(kb)" align="center"></el-table-column>
            <el-table-column prop="ownerid" label="所属记录员id" align="center"></el-table-column>
            <el-table-column prop="carid" label="所属车辆id" align="center"></el-table-column>
            <el-table-column label="预览" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" @click="watchvideo(scope.row.name)">查看</el-button>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-popconfirm
                            class="ml-5"
                            confirm-button-text='确定'
                            cancel-button-text='我再想想'
                            icon="el-icon-info"
                            icon-color="red"
                            title="您确定删除吗？"
                            @confirm="del(scope.row.id)"
                    >
                        <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i>
                        </el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <div style="padding: 10px 0">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageNum"
                    :page-sizes="[2, 5, 10, 20]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>

        <el-dialog title="视频信息" :visible.sync="videoVisible" width="50%">
            <div>

                <el-card >
                    <video-player style="width: 100%;height: 100%;margin:0 auto;box-shadow:5px 5px 8px #888888"
                                  class="video-player vjs-custom-skin"
                                  ref="videoPlayer"
                                  :playsinline="true"
                                  :options="playerOptions"
                    ></video-player>
                </el-card>

            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="videoVisible = false">取 消</el-button>
            </div>

        </el-dialog>
    </div>
</template>

<script>
import {serverIp} from "../../public/config";
import request from "@/utils/request";

export default {
    name: "video",
    data() {
        return {
            serverIp: serverIp,
            tableData: [],
            abdata: [],
            videotime: 10,
            name: '',
            multipleSelection: [],
            pageNum: 1,
            pageSize: 10,
            form:{},
            videoVisible: 0,
            path: "/home/dd/DMS/videos/",
            total: 0,
            playerOptions: {
                playbackRates: [0.7, 1.0, 1.5, 2.0], //播放速度
                autoplay: false, //如果true,浏览器准备好时开始回放。
                muted: false, // 默认情况下将会消除任何音频。
                loop: false, // 导致视频一结束就重新开始。
                preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
                language: 'zh-CN',
                aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
                fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
                sources: [{
                    type: "video/mp4",
                    src: "" //url地址
                }],
                poster: "", //你的封面地址
                // width: document.documentElement.clientWidth,
                notSupportedMessage: '此视频暂无法播放，请稍后再试', //允许覆盖Video.js无法播放媒体源时显示的默认信息。
                controlBar: {
                    timeDivider: true,
                    durationDisplay: true,
                    remainingTimeDisplay: false,
                    fullscreenToggle: true  //全屏按钮
                }
            },
        }
    },
    created() {
        this.load()
    },

    methods: {
        watchvideo(id) {
             this.playerOptions.sources[0].src = require("../../../mysql" + "/videos/" + id)
            //this.playerOptions.sources[0].src = require("../../../files/videos/res1101.mp4")
            //this.$message.success(this.playerOptions.sources[0].src)
            this.videoVisible = true

        },
        load() {
            this.request.get("/video/page", {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    name: this.name,
                }
            }).then(res => {
                this.tableData = res.data.records
                this.total = res.data.total
            })
            this.request.get("/video/page", {}).then(res => {
                this.videotime = res.data.value
            })

        },
        changeEnable(row) {
            this.request.post("/video/update", row).then(res => {
                if (res.code === '200') {
                    this.$message.success("操作成功")
                }
            })
        },
        del(id) {
            this.request.delete("/video/" + id).then(res => {
                if (res.code === '200') {
                    this.$message.success("删除成功")
                    this.load()
                } else {
                    this.$message.error("删除失败")
                }
            })
        },
        handleSelectionChange(val) {
            console.log(val)
            this.multipleSelection = val
        },
        delBatch() {
            let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
            this.request.post("/video/del/batch", ids).then(res => {
                if (res.code === '200') {
                    this.$message.success("批量删除成功")
                    this.load()
                } else {
                    this.$message.error("批量删除失败")
                }
            })
        },
        reset() {
            this.name = ""
            this.load()
        },

        handleSizeChange(pageSize) {
            console.log(pageSize)
            this.pageSize = pageSize
            this.load()
        },
        handleCurrentChange(pageNum) {
            console.log(pageNum)
            this.pageNum = pageNum
            this.load()
        },
        handleFileUploadSuccess(res) {
            console.log(res)
            this.$message.success("上传成功")
            this.load()
        },
        download(url) {
            window.open(url)
        },
        preview(url) {
            window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
        },
        changevideotime() {
            this.$message.info("新的视频保存时间为：" + this.videotime)
        }
    }
}
</script>
<style scoped>
</style>