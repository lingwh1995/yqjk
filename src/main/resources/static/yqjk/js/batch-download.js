$(function () {
    /**
     * 下载excel模板
     */
    $('.yqjk .batch-download .download-pattern').on('click',function () {
        var downloadUrl = $.fn.contextPath + '/yqjk/file/file.xls';
        var $form = $('<form></form>');
        $form.addClass('download-temp-form');
        $form.attr('action',downloadUrl);
        $form.attr('method','get');
        $form.appendTo("body");
        $form.css('display','none');
        $form.submit();
        $(".download-temp-form").remove();
    });

    /**
     * 导入excel
     */
    var taskCache = new Array();
    $('.yqjk .batch-download .upload-excel').click(function(){
        var uploadefile = $("#file").get(0).files[0];
        /**
         * 判断文件格式
         */
        if(typeof(uploadefile) == 'undefined'){
            return;
        }else {
            var fileName = uploadefile.name;
            if(fileName.indexOf(".") != -1){
                var suffix = fileName.substring(fileName.lastIndexOf("."));
                if(suffix != '.xls'){
                    layer.msg('您上传的文件格式不正确!');
                    //重置上传组件
                    $("#uploader-form")[0].reset();
                    return;
                }
            }else{
                layer.msg('您上传的文件格式不正确!');
                //重置上传组件
                $("#uploader-form")[0].reset();
                return;
            }
        }
        var identifier = '';
        for(var i=0; i<taskCache.length; i++){
            if(uploadefile.name == taskCache[i].fileName && uploadefile.size == taskCache[i].fileSize){
                identifier = taskCache[i].identifier;
                layer.msg('您已经导入过此Excel文件了！');
                return;
            }
        }
        //开启layer弹出层
        var loading = layer.load(0,{shade: [0.2,'#FAEBD7'] });
        var fd = new FormData();
        fd.append("file", uploadefile);
        fd.append("identifier", identifier);
        $.ajax({
            url:$.fn.contextPath + "/parse-excel.action",
            type:"post",
            data:fd,
            cache: false,
            processData: false,
            contentType: false,
            success:function(result){
                if(result.statusCode == 200){
                    //隐藏示例部分
                    $(".batch-download .demo").hide();
                    //把后台返回的任务信息缓存到前台
                    var task = JSON.parse(result.message);
                    if(taskCache.length ==0){
                        renderTaskList(task);
                    }
                    for(var i=0; i<taskCache.length; i++){
                        if(! (task.fileName == taskCache[i].fileName && task.fileSize == taskCache[i].fileSize)){
                            renderTaskList(task);
                        }else{
                            layer.msg('您已经导入过此Excel文件了！');
                        }
                    }
                    taskCache.push(task);
                    //每隔timeInterval毫秒就给后台发送一次ajax请求,使用返回的数据渲染前台的进度条
                    var timeInterval = task.batchQueryRateRefreshTimeInterval;
                    /**
                     * 渲染可能密切接触进度条
                     */
                    $(".knmqjcz a").on('click',function() {
                        var identifier = $(this).attr('identifier');
                        var getExportKnmqjczRate = setInterval($.proxy(function () {
                            $.ajax({
                                url:$.fn.contextPath + "/export-excel-rate-knmqjcz.action?identifier="+identifier,
                                async:false,
                                type:'get',
                                success : function(data) {
                                    if(data <= task.conditionSize){
                                        var rate = (data/task.conditionSize * 100) +'%';
                                        $(".knmqjcz .progress-bar").width(rate);
                                        if(rate == '100%'){
                                            //清除定时器
                                            clearInterval(getExportKnmqjczRate);
                                            //删除Redis中缓存进度数据
                                            $.ajax({
                                                url:$.fn.contextPath + "/clear-rate-data.action?identifier="+identifier+'&type=0',
                                                type:'get',
                                                ssuccess : function(data) {

                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        },this),timeInterval);
                    });
                    /**
                     * 渲染确认和疑似病例进度条
                     */
                    $(".qzhysbl a").on('click',function() {
                        var getExportQzhysblRate = setInterval($.proxy(function () {
                            var identifier = $(this).attr('identifier');
                            $.ajax({
                                url:$.fn.contextPath + "/export-excel-rate-qzhysbl.action?identifier="+identifier,
                                async:false,
                                type:'get',
                                success : function(data) {
                                    if(data <= task.conditionSize){
                                        var rate = (data/task.conditionSize * 100) +'%';
                                        $(".qzhysbl .progress-bar").width(rate);
                                        if(rate == '100%'){
                                            //清除定时器
                                            clearInterval(getExportQzhysblRate);
                                            //删除Redis中缓存进度数据
                                            $.ajax({
                                                url:$.fn.contextPath + "/clear-rate-data.action?identifier="+identifier+'&type=1',
                                                type:'get',
                                                ssuccess : function(data) {

                                                }
                                            });
                                        }
                                    }
                                }

                            });
                        },this),timeInterval);
                    });
                }else if(result.statusCode == 300) {
                    layer.alert(result.message, {icon: 2});
                }
                //渲染完成后关闭loading层
                layer.close(loading);
             },
            error:function(e){
                layer.msg("网络错误，请重试！");
            }
        });
    });

    /**
     * 渲染下载列表
     * @param task
     */
    function renderTaskList(task) {
        var html =
            ' <tr class="knmqjcz">\n' +
            '     <td rowspan="2" style="vertical-align: middle;font-weight: 700">'+task.fileName+'</td>\n' +
            '     <td>可能密切接触者</td>\n' +
            '     <td class="progress" style="background-color: #FFFFFF;">\n' +
            '         <div class="progress-bar" style="width: 0%;"></div>\n' +
            '     </td>\n' +
            '     <td>\n' +
            '         <a class="btn btn-success btn-xs" identifier="'+task.identifier+'"' +
            '                href="'+ $.fn.contextPath+'/export-excel-knmqjcz.action?identifier='+task.identifier+'">导出</a><br/>\n' +
            '     </td>\n' +
            ' </tr>\n' +
            ' <tr class="qzhysbl">\n' +
            '     <td>确诊和疑似病例</td>\n' +
            '     <td class="progress" style="background-color: #FFFFFF;">\n' +
            '          <div class="progress-bar" style="width: 0%;"></div>\n' +
            '     </td>\n' +
            '     <td>\n' +
            '          <a class="btn btn-success btn-xs" identifier="'+task.identifier+'"' +
            '                href="'+ $.fn.contextPath +'/export-excel-qzhysbl.action?identifier='+task.identifier+'">导出</a><br/>\n' +
            '     </td>\n' +
            ' </tr>';
        $('.batch-download .download-info').append(html);
    }

    $(".yqjk .batch-download .demo .export-btn").click(function () {
        layer.msg('请先导入excel文件,再点击此按钮！');
    });
});