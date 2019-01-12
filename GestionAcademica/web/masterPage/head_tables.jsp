<%-- 
    Document   : head
    Created on : 24-jun-2017, 11:59:53
    Author     : alvar
--%>

<%@page import="Enumerado.IconClass"%>
<!--
    ARCHIVOS NECESARIOS PARA REPORTES Y DATATABLES RESPONSIVE
-->

<script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Buttons/js/dataTables.buttons.min.js"></script>
<script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Buttons/js/buttons.html5.min.js"></script>
<script src="<%=request.getContextPath()%>/JavaScript/jszip/jszip.min.js"></script>
<script src="<%=request.getContextPath()%>/JavaScript/pdfmake/pdfmake.min.js"></script>
<script src="<%=request.getContextPath()%>/JavaScript/pdfmake/vfs_fonts.js"></script>

<script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/js/dataTables.responsive.min.js"></script>
<link href="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet" type="text/css"/>



<script>
    $('#tbl_ww').hide();
    MostrarCargando(true);
    
    $(document).ready(function() {

            
            
            //---------------------------------------------------------
            //TABLA CON REPORTES
            //---------------------------------------------------------
            $('#tbl_ww').DataTable({
                "responsive": true,
                "processing": true,
                deferRender: true,
                "lengthMenu": [ [10, 20, 50, -1], [10, 20, 50, "Todos"] ],
                pageLength: 20,
                "fnInitComplete": function(oSettings, json) {
                        MostrarCargando(false);
                        $('#tbl_ww').show();
                      },
                "columnDefs": [ {
                    "targets": 0,
                    "orderable": false
                    },{
                    "targets": 1,
                    "orderable": false
                    } ],
                "order": [[ 2, "asc" ]],
                dom: 'Blfrtip',
                buttons: [
                    {
                        extend: 'copyHtml5',
                        className:'<%=IconClass.COPY.getValor()%> accion_icon',
                        text:''
                    },
                    {
                        extend: 'csvHtml5',
                        className:'<%=IconClass.CSV.getValor()%> accion_icon',
                        text:''
                    },
                    {
                        extend: 'excelHtml5',
                        className:'<%=IconClass.EXCEL.getValor()%> accion_icon',
                        text:''
                    },
                    {
                        extend: 'pdfHtml5',
                        orientation: 'landscape',
                        pageSize: 'LEGAL',
                        className:'<%=IconClass.PDF.getValor()%> accion_icon',
                        text:''
                    }
                ],
                "language": {
                        "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                    }
            });

    } );
</script>