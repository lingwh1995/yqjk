(function($){
    /**
     * 获取请求地址前缀
     * @returns {string}
     */
    function getContextPath() {
        var curWwwPath = window.location.href;
        var pathName = window.location.pathname;
        var local = curWwwPath.substring(0,curWwwPath.indexOf(pathName));
        var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        return local + projectName;
    }
    $.fn.contextPath = getContextPath();
})(window.jQuery);