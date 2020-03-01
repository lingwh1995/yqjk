$(function () {
    /**
     * 检验表单
     */
    function validateForm(_condition) {
        if(_condition.xm == '' && _condition.sfzhm == ''){
            layer.msg("请输入查询条件！");
            $('.yqjk .list .qzhysbl .data,.yqjk .list .knmqjcz .data')
                .html('<tr><td colspan="5" class="message">请输入查询条件并点击查询按钮......</td></tr>');
            $(".yqjk .list .condition .cxsy").val('');
            return false;
        }
        if(_condition.xm == '') {
            layer.msg('请您输入姓名后再进行查询！');
            return false;
        }else{
            if(_condition.xm.length > 15){
                layer.msg("您输入的姓名长度超过了15个字符！");
                return false;
            }
        }
        if(_condition.cxsy.length > 15){
            layer.msg("您输入的查询事由长度超过了15个字符！");
            return false;
        }
        if(_condition.sfzhm == '') {
            layer.msg('请您输入身份证号码后再进行查询！');
            return false;
        }else{
            var idCardReg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
            if (! idCardReg.test(_condition.sfzhm)){
                layer.msg('您输入的身份证格式不正确！');
                return false;
            }
        }
        return true;
    }

    /**
     * 执行查询操作
     */
    $(".yqjk .list .condition .search").click(function () {
        $('.yqjk .list .knmqjcz .data tr').html('<td colspan="5" >暂无匹配记录......</td>');
        $('.yqjk .list .qzhysbl .data tr').html('<td colspan="5" >暂无匹配记录......</td>');
        var _condition = {
            xm: $(".yqjk .list .condition .xm").val().trim(),
            sfzhm: $(".yqjk .list .condition .sfzhm").val().trim(),
            cxsy: $(".yqjk .list .condition .cxsy").val().trim(),
        };
        if(! validateForm(_condition)){
            return;
        }
        var loading = layer.load(0,{shade: [0.2,'#FAEBD7'] });

        /**
         * 并发调用
         */
        /**
         * 调用可能密切接触者数据查询服务接口的Ajax对象
         */
        var knmqjczAjax = $.ajax({type:'get',async:'true',data:_condition,url:$.fn.contextPath + '/knmqjcz-list.action?userId='+$("#userId").val()});
        /**
         * 调用确诊和疑似病例数据查询服务接口的Ajax对象
         */
        var qzhysblAjax = $.ajax({type:'get',async:'true',data:_condition,url:$.fn.contextPath + '/qzhysbl-list.action?userId='+$("#userId").val()});
        /**
         *  $.when()并发调用后端接口,调用成功后可以同时渲染前台数据,这样就不会出现
         *  先渲染一部分数据再渲染一部分数据的情况了
         *  @param knmqjczAjax 调用可能密切接触者数据查询服务接口的Ajax对象
         *  @param qzhysbl 调用确诊和疑似病例数据查询服务接口的Ajax对象
         */
        $.when(knmqjczAjax,qzhysblAjax)
            .done(function(knmqjcz,qzhysbl){
                renderKnmqjcz(knmqjcz[0]);
                renderQrhysb(qzhysbl[0]);
                //渲染完成后关闭loading层
                layer.close(loading);
            });
    });

    /**
     * 渲染能密切接触者列表
     * @param resultOfknmqjcz 可能密切接触者数据查询服务接口返回的json数据
     */
    function renderKnmqjcz(resultOfknmqjcz) {
    resultOfknmqjcz = JSON.parse(resultOfknmqjcz);
        if(resultOfknmqjcz.code == '301'){
            $('.yqjk .list .knmqjcz .data tr').html('<td colspan="5" >查询接口繁忙请稍后再试......</td>');
            return;
        }
        if(resultOfknmqjcz.code == '200'){
            if(resultOfknmqjcz.data.length > 0) {
                var knmqjczList = '';
                resultOfknmqjcz.data.forEach(function (element) {
                    knmqjczList += '<tr>';
                    knmqjczList += '<td>' + element.xm + '</td>';
                    knmqjczList += '<td>' + element.sfzhm + '</td>';
                    knmqjczList += '<td>' + element.jcrq + '</td>';
                    knmqjczList += '<td>' + element.jclx + '</td>';
                    knmqjczList += '<td>' + (resultOfknmqjcz.isContactPerson == '1' ? '是' : '否') + '</td>';
                    knmqjczList += '</tr>';
                });
                //渲染完成后给表格边框增加样式
                $('.yqjk .list .knmqjcz .data').html(knmqjczList);
            }else{
                $('.yqjk .list .knmqjcz .message').text('暂无匹配记录......');
            }
        }
    }

    /**
     * 渲染确诊和疑似病例列表
     * @param resultOfqzhysbl 调用确诊和疑似病例数据查询服务接口返回的json数据
     */
    function renderQrhysb(resultOfqzhysbl) {
        resultOfqzhysbl = JSON.parse(resultOfqzhysbl);
        if(resultOfqzhysbl.code == '301'){
            $('.yqjk .list .qzhysbl .data tr').html('<td colspan="5" >查询接口繁忙请稍后再试......</td>');
            return;
        }
        if(resultOfqzhysbl.code == '200'){
            if(! $.isEmptyObject(resultOfqzhysbl.data)){
                var qzhysblList = '';
                qzhysblList += '<tr>';
                qzhysblList+= '<td>'+resultOfqzhysbl.data.xm+'</td>';
                qzhysblList+= '<td>'+resultOfqzhysbl.data.sfzhm+'</td>';
                qzhysblList+= '<td>'+resultOfqzhysbl.data.fbsj+'</td>';
                qzhysblList+= '<td>'+resultOfqzhysbl.data.qzsj+'</td>';
                qzhysblList+= '<td>'+resultOfqzhysbl.data.bllx+'</td>';
                qzhysblList += '</tr>';
                //渲染完成后给表格边框增加样式
                $('.yqjk .list .qzhysbl .data').html(qzhysblList);
            }else{
                $('.yqjk .list .qzhysbl .message').text('暂无匹配记录......');
            }
        }
    }

    $(".yqjk .list .condition .batch-download").click(function(){
        window.open($.fn.contextPath + '/batch-download.action?userId='+$("#userId").val(),'_blank');
    });
});