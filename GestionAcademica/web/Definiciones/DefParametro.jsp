<%-- 
    Document   : DefVersion
    Created on : 25-jun-2017, 21:43:57
    Author     : alvar
--%>

<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
    String usuario = (String) session.getAttribute(Enumerado.NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Logica.Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    Parametro parametro = LoParametro.GetInstancia().obtener();

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Versión</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <script>
            $(document).ready(function () {

                $('#btn_guardar').click(function (event) {
                    if(validarDatos())
                    {
                        procesarDatos();
                    }
                });
                
                function validarDatos(){
                    
                    if(!$('#frm_general')[0].checkValidity())
                    {
                        var $myForm = $('#frm_general');
                        $myForm.find(':submit').click();
                        return false;
                    }

                    return true;
                }
                
                function procesarDatos() {


                    var ParCod = $('#ParCod').val();
                    var ParEmlCod = $('#ParEmlCod').val();
                    var ParSisLocal = document.getElementById('ParSisLocal').checked;
                    var ParUtlMdl = document.getElementById('ParUtlMdl').checked;
                    var ParUrlMdl = $('#ParUrlMdl').val();
                    var ParMdlTkn = $('#ParMdlTkn').val();
                    var ParUrlSrvSnc = $('#ParUrlSrvSnc').val();
                    var ParPswValExp = $('#ParPswValExp').val();
                    var ParPswValMsg = $('#ParPswValMsg').val();
                    var ParDiaEvlPrv = $('#ParDiaEvlPrv').val();
                    var ParTieIna = $('#ParTieIna').val();
                    var ParSncAct = document.getElementById('ParSncAct').checked;


                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>AM_Parametro', {
                        pParCod: ParCod,
                        pParEmlCod: ParEmlCod,
                        pParSisLocal: ParSisLocal,
                        pParUtlMdl: ParUtlMdl,
                        pParUrlMdl: ParUrlMdl,
                        pParMdlTkn: ParMdlTkn,
                        pParUrlSrvSnc: ParUrlSrvSnc,
                        pParPswValExp: ParPswValExp,
                        pParPswValMsg: ParPswValMsg,
                        pParDiaEvlPrv: ParDiaEvlPrv,
                        pParTieIna: ParTieIna,
                        pParSncAct: ParSncAct,
                        pAction: "ACTUALIZAR"

                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                    });

                }

            });
        </script>

    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <jsp:include page="/masterPage/cabezal_menu.jsp"/>
		<!-- CONTENIDO -->
        <div class="contenido" id="contenedor">                
            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                        <header class="panel-heading">
                            <!-- TITULO -->
                                PARAMETROS
                            <!-- BOTONES -->
                        </header>
        
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            
                                                            <input type="hidden" id="ParCod" name="ParCod" placeholder="Código" disabled value="<% out.print(utilidad.NuloToVacio(parametro.getParCod())); %>">

                                                            <div class="form-group "><label for="ParUrlSis" class="control-label col-lg-3">URL del sistema</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="ParUrlSis" name="ParUrlSis"  value="<%=utilidad.NuloToVacio(parametro.getParUrlSis())%>" ></div></div>
                                                            
                                                            <div class="form-group "><label for="ParPswValExp" class="control-label col-lg-3">Expresión regular para validar contraseña</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="ParPswValExp" name="ParPswValExp"  value="<%=utilidad.NuloToVacio(parametro.getParPswValExp())%>" ></div></div>
                                                            <div class="form-group "><label for="ParPswValMsg" class="control-label col-lg-3">Mensaje en caso de contraseña incorrecta</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="ParPswValMsg" name="ParPswValMsg"  value="<%=utilidad.NuloToVacio(parametro.getParPswValMsg())%>" ></div></div>
                                                            
                                                            
                                                            
                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Notificación</h2>
                                                            </div>
                                                            
                                                            <div class="form-group ">
                                                                <label for="ParEmlCod" class="control-label col-lg-3">Email</label>
                                                                <div class="col-lg-1">
                                                                    <input type="number" class=" form-control inputs_generales" id="ParEmlCod" name="ParEmlCod"  value="<% out.print(utilidad.NuloToVacio((parametro.getParametroEmail() != null ? parametro.getParametroEmail().getParEmlCod() : ""))); %>" disabled >
                                                                </div>
                                                                <div class="col-lg-2">
                                                                    <a href="#" id="btnParEmlCod" name="btnParEmlCod" class="glyphicon glyphicon-search" data-toggle="modal" data-target="#PopUpParamEml"></a>
                                                                </div>

                                                                <div class="col-lg-3">
                                                                    <label name="ParEmlNom" id="ParEmlNom" > <% out.print(utilidad.NuloToVacio((parametro.getParametroEmail() != null ? parametro.getParametroEmail().getParEmlNom() : null))); %></label>
                                                                </div>
                                                            </div>
                                                                
                                                            <div class="form-group "><label for="ParDiaEvlPrv" class="control-label col-lg-3">Dias de anticipación para notificar fecha proxima de evaluacion</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="ParDiaEvlPrv" name="ParDiaEvlPrv"  value="<%=utilidad.NuloToVacio(parametro.getParDiaEvlPrv())%>" ></div></div>
                                                            <div class="form-group "><label for="ParTieIna" class="control-label col-lg-3">Tiempo de inactividad en meses, para notificacion de materias previas (Not. Motivacional)</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="ParTieIna" name="ParTieIna"  value="<%=utilidad.NuloToVacio(parametro.getParTieIna())%>" ></div></div>
                                                            
                                                            
                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Moodle</h2>
                                                            </div>
                                                            <div class="form-group "><label for="ParUtlMdl" class="control-label col-lg-3">Utiliza Moodle</label><div class="col-lg-6"><input type="checkbox"  id="ParUtlMdl" name="ParUtlMdl"  <%=utilidad.BooleanToChecked(parametro.getParUtlMdl())%> ></div></div>
                                                            <div class="form-group "><label for="ParUrlMdl" class="control-label col-lg-3">URL de Moodle</label><div class="col-lg-6"><input type="url" class=" form-control inputs_generales" id="ParUrlMdl" name="ParUrlMdl"  value="<%=utilidad.NuloToVacio(parametro.getParUrlMdl())%>" ></div></div>
                                                            <div class="form-group "><label for="ParMdlTkn" class="control-label col-lg-3">Token de Moodle</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="ParMdlTkn" name="ParMdlTkn"  value="<%=utilidad.NuloToVacio(parametro.getParMdlTkn())%>" ></div></div>
                                                            
                                                            
                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Sincronización</h2>
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="ParSncAct" class="control-label col-lg-3">Sincronización activa</label><div class="col-lg-6"><input type="checkbox" id="ParSncAct" name="ParSncAct"  <%=utilidad.BooleanToChecked(parametro.getParSncAct())%> ></div></div>
                                                            <div class="form-group "><label for="ParSisLocal" class="control-label col-lg-3">Sistema local</label><div class="col-lg-6"><input type="checkbox"  id="ParSisLocal" name="ParSisLocal"  <%=utilidad.BooleanToChecked(parametro.getParSisLocal())%> ></div></div>
                                                            <div class="form-group "><label for="ParUrlSrvSnc" class="control-label col-lg-3">URL Sistema Online</label><div class="col-lg-6"><input type="url" class=" form-control inputs_generales" id="ParUrlSrvSnc" name="ParUrlSrvSnc"  value="<%=utilidad.NuloToVacio(parametro.getParUrlSrvSnc())%>" ></div></div>
                                                            <div class="form-group "><label for="ParFchUltSinc" class="control-label col-lg-3">Última sincronización</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="ParFchUltSinc" name="ParFchUltSinc"  disabled value="<%=utilidad.NuloToVacio(parametro.getParFchUltSinc())%>" ></div></div>
                                                                                                                        
                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input name="btn_guardar" id="btn_guardar"  type="button"  class="btn btn-primary" value="MODIFICAR" />
                                                                </div>
                                                            </div>
                                                            
                                                        </form>
                                                    </div>
                                                </div>
                                            </section>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>
        
        <div id="PopUpParamEml" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Parámetro de email</h4>
                    </div>

                    <div class="modal-body">

                        <div>
                            <table name="PopUpTblParEml" id="PopUpTblParEml" class="table table-striped" cellspacing="0"  class="table" width="100%">
                                <thead>
                                    <tr>
                                        <th>Código</th>
                                        <th>Nombre</th>
                                        <th>Correo saliente</th>
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

                $(document).ready(function () {

                    $(document).on('click', ".PopParamr_Seleccionar", function () {

                        var ParEmlCod = $(this).data("codigo");
                        var ParEmlNom = $(this).data("nombre");

                        $('#ParEmlCod').val(ParEmlCod);
                        $('#ParEmlNom').text(ParEmlNom);


                        $(function () {
                            $('#PopUpParamEml').modal('toggle');
                        });
                    });


                    $.post('<% out.print(urlSistema);%>ABM_ParametroEmail', {
                        pAction: "POPUP_OBTENER"
                    }, function (responseText) {

                        var parametros = JSON.parse(responseText);

                        $.each(parametros, function (f, param) {

                            param.parEmlCod = "<td> <a href='#' data-codigo='" + param.parEmlCod + "' data-nombre='" + param.parEmlNom + "' class='PopParamr_Seleccionar'>" + param.parEmlCod + " </a> </td>";
                        });

                        $('#PopUpTblParEml').DataTable({
                            data: parametros,
                            deferRender: true,
                            bLengthChange: false, //thought this line could hide the LengthMenu
                            pageLength: 10,
                            language: {
                                "lengthMenu": "Mostrando _MENU_ registros por página",
                                "zeroRecords": "No se encontraron registros",
                                "info": "Página _PAGE_ de _PAGES_",
                                "infoEmpty": "No hay registros",
                                "search": "Buscar:",
                                "paginate": {
                                    "first": "Primera",
                                    "last": "Ultima",
                                    "next": "Siguiente",
                                    "previous": "Anterior"
                                },
                                "infoFiltered": "(Filtrado de _MAX_ total de registros)"
                            }
                            , columns: [
                                {"data": "parEmlCod"},
                                {"data": "parEmlNom"},
                                {"data": "parEmlDeEml"}
                            ]

                        });

                    });



                });
            </script>
        </div>
    </body>
</html>
