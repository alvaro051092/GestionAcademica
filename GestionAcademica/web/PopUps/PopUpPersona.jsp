<%-- 
    Document   : PopUpEvaluacion
    Created on : 12-jul-2017, 10:55:35
    Author     : alvar
--%>

<%@page import="Entidad.Curso"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Logica.LoCurso"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%

    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();
    

%>

<!-- Modal -->
<div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Personas</h4>
      </div>
      <div class="modal-body">
        
          
          
            <div>
                
                
                
                <table id="example" class="display" cellspacing="0"  class="table" width="100%">
                    <thead>
                        <tr>
                            <th>Codigo</th>
                            <th>Nombre</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th>Codigo</th>
                            <th>Nombre</th>
                        </tr>
                    </tfoot>
                </table>
            </div>

           
          
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
</div>
    
    
    
    <script type="text/javascript">
        
        $(document).ready(function() {
            
            Buscar();
            
            $("#PopPer_btnBuscar").click(function(){
                Buscar();
            });
            
            
          
        
        $(document).on('click', ".PopPer_Seleccionar", function() {
            
           // alert($(this).data("codigo"));
           
           $('#AluPerCod').val($(this).data("codigo"));
           
            $(function () {
                    $('#PopUpPersona').modal('toggle');
                 });
                 
/*
                $('#EvlCod').val($(this).data("codigo"));
                $('#EvlNom').val($(this).data("nombre"));
                
                $(function () {
                    $('#PopUpPersona').modal('toggle');
                 });
                //$("#PopUpPersona").dialog("close");       
                */
        });
        
        
        function Buscar()
        {
                var PerNom = $('#popPerNom').val();
                
                $.post('<% out.print(urlSistema); %>ABM_Persona', {
                popPerNom : PerNom,        
                pAction : "POPUP_OBTENER"
                    }, function(responseText) {
                
                        var odd_even    = false;
                        var tbl_row     = "";
                        var tbl_body    = "";

                        var personas = JSON.parse(responseText);

                /*

                        $.each(JSON.parse(responseText), function(f , persona) {

                                tbl_row = "<td> <a href='#' data-codigo='"+persona.perCod+"' data-nombre='"+persona.perNom+"' class='PopPer_Seleccionar'>"+persona.perCod+" </a> </td>";
                                tbl_row += "<td>"+persona.perNom+"</td>";

                                tbl_body += "<tr class=\""+( odd_even ? "odd" : "even")+"\">"+tbl_row+"</tr>";
                                odd_even = !odd_even;            
                            });

                        $("#PopUp_TblPersona").html(tbl_body);
                 $('#example').DataTable( {
                        "ajax": JSON.parse(responseText),
                        "columns": [
                            { "data": "perCod" },
                            { "data": "perNom" }
                        ]
                    } );
                    
                  */  
                 
                 $.each(personas, function(f , persona) {

                                persona.perCod = "<td> <a href='#' data-codigo='"+persona.perCod+"' data-nombre='"+persona.perNom+"' class='PopPer_Seleccionar'>"+persona.perCod+" </a> </td>";
                 });
                 
                    $('#example').DataTable( {
                        data: personas,
                        deferRender: true,
                        language: {
                            "lengthMenu": "Mostrando _MENU_ registros por página",
                            "zeroRecords": "No se encontraron registros",
                            "info": "Página _PAGE_ de _PAGES_",
                            "infoEmpty": "No hay registros",
                            "infoFiltered": "(Filtrado de _MAX_ total de registros)"
                        }
                        ,columns: [
                            { "data": "perCod" },
                            { "data": "perNom"}
                        ]
                        
                    } );

                });
        }
        

        });
        </script>
        
        
        
        
       