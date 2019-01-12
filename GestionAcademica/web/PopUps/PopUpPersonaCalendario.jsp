<%-- 
    Document   : PopUpEvaluacion
    Created on : 12-jul-2017, 10:55:35
    Author     : alvar
--%>

<%@page import="Enumerado.Modo"%>
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
                
                
                
                <table id="PopUpTblPersona" name="PopUpTblPersona" class="table table-striped" cellspacing="0"  class="table" width="100%">
                    <thead>
                        <tr>
                            <th>Codigo</th>
                            <th>Nombre</th>
                            <th>Tipo</th>
                            <th>Documento</th>
                        </tr>
                    </thead>
                    
                </table>
            </div>

           
          
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
      </div>
    </div>
</div>
    
    
    
    <script type="text/javascript">
        
        $(document).ready(function() {
            
            Buscar();
          
        
            $(document).on('click', ".PopPer_Seleccionar", function() {

               var AluPerCod = $(this).data("codigo");
               var CalCod = $('#CalCod').val();
               $.post('<% out.print(urlSistema); %>ABM_CalendarioAlumno', {
                            pCalCod: CalCod,
                            pAluPerCod: AluPerCod,
                            pAction: "<% out.print(Modo.INSERT);%>"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);
                            MostrarCargando(false);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                location.reload();
                            } else
                            {
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                            }

                        });

                $(function () {
                        $('#PopUpPersona').modal('toggle');
                     });
            });


            function Buscar()
            {
                    var PerNom = $('#popPerNom').val();

                    $.post('<% out.print(urlSistema); %>ABM_Persona', {
                    popPerNom : PerNom,        
                    pAction : "POPUP_OBTENER"
                        }, function(responseText) {

                        
                            var personas = JSON.parse(responseText);

                            $.each(personas, function(f , persona) {

                                           persona.perCod = "<td> <a href='#' data-codigo='"+persona.perCod+"' data-nombre='"+persona.perNom+"' class='PopPer_Seleccionar'>"+persona.perCod+" </a> </td>";
                            });

                            $('#PopUpTblPersona').DataTable( {
                                data: personas,
                                deferRender: true,
                                bLengthChange : false, //thought this line could hide the LengthMenu
                                pageLength: 10,
                                language: {
                                    "lengthMenu": "Mostrando _MENU_ registros por página",
                                    "zeroRecords": "No se encontraron registros",
                                    "info": "Página _PAGE_ de _PAGES_",
                                    "infoEmpty": "No hay registros",
                                    "search":         "Buscar:",
                                    "paginate": {
                                            "first":      "Primera",
                                            "last":       "Ultima",
                                            "next":       "Siguiente",
                                            "previous":   "Anterior"
                                        },
                                    "infoFiltered": "(Filtrado de _MAX_ total de registros)"
                                }
                                ,columns: [
                                    { "data": "perCod" },
                                    { "data": "nombreCompleto"},
                                    { "data": "tipoPersona"},
                                    { "data": "perDoc"}
                                ]

                            } );

                    });
            }
        

        });
        </script>
        
        
        
        
       