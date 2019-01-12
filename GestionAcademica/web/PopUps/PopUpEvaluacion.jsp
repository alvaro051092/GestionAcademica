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
    String urlSistema   = utilidad.GetUrlSistema();
    
    LoCurso loCurso     = LoCurso.GetInstancia();
    
    List<Object> lstCurso = new ArrayList<>();
    
    Retorno_MsgObj retorno = (Retorno_MsgObj) loCurso.obtenerLista();
    if(!retorno.SurgioErrorListaRequerida())
    {
        lstCurso = retorno.getLstObjetos();
    }
    else
    {
        out.print(retorno.getMensaje().toString());
    }
    

%>

            <div class="modal-dialog" >
                <!-- Modal content-->
                <div class="modal-content modal-lg">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Evaluaciones</h4>
                    </div>

                    <div class="modal-body">

                        <div>
                            <table name="PopUpTblEvaluaciones_" id="PopUpTblEvaluaciones_" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                    <tr>
                                        <th>Carrera / Curso</th>
                                        <th>Estudio</th>
                                        <th>Evaluación</th>
                                    </tr>
                                </thead>

                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-default" value="Cancelar" data-dismiss="modal" />
                    </div>
                </div>
            </div>


 <script type="text/javascript">
        
        $(document).ready(function() {
            
           
            $(document).on('click', ".Pop_Seleccionar", function() {

                    $('#EvlCod').val($(this).data("codigo"));
                    $('#EvlNom').val($(this).data("nombre"));

                    $(function () {
                        $('#PopUpEvaluacion').modal('toggle');
                     });
                    //$("#PopUpEvaluacion").dialog("close");       
            });
            
            $.post('<% out.print(urlSistema); %>ABM_Evaluacion', {
                        pAction: "POPUP_LISTAR"
                    }, function (responseText) {

                        var evaluaciones = JSON.parse(responseText);
                        
                        $.each(evaluaciones, function (f, evl) {
                                evl.estudioNombre = "<a href='#' data-codigo='" + evl.evlCod + "' data-nombre='" + evl.evlNom + "' class='Pop_Seleccionar'>" + evl.estudioNombre + " </a> ";
                            });

                        $('#PopUpTblEvaluaciones_').DataTable({
                            data: evaluaciones,
                            responsive: true,
                            processing: true,
                            deferRender: true,
                            bLengthChange: false, //thought this line could hide the LengthMenu
                            pageLength: 10,
                            destroy: true,
                            language: {
                                    "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                                },
                            columns: [
                                {"data": "carreraCursoNombre"},
                                {"data": "estudioNombre"},
                                {"data": "evlNom"}
                            ]

                        });

                    });
        

        });
        </script>