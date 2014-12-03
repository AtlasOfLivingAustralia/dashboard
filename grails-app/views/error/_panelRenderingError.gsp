<script>
    $(function () {
        var fileName = '${gspFile}';
        if(dashboard.panelRenderingErrors.indexOf(fileName) == -1) {
            dashboard.panelRenderingErrors.push(fileName);
        }
    });

</script>