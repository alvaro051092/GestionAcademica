<%-- 
    Document   : DefModulo
    Created on : 04-jul-2017, 19:31:27
    Author     : alvar
--%>

<%@page import="Entidad.NotificacionDestinatario"%>
<%@page import="Enumerado.TipoRepeticion"%>
<%@page import="Enumerado.TipoEnvio"%>
<%@page import="Enumerado.ObtenerDestinatario"%>
<%@page import="Enumerado.TipoNotificacion"%>
<%@page import="Logica.LoNotificacion"%>
<%@page import="Entidad.Notificacion"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.TipoPeriodo"%>
<%@page import="Entidad.Modulo"%>
<%@page import="Entidad.Curso"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoCurso"%>
<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
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
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String NotCod = request.getParameter("pNotCod");
    String NotDstCod = request.getParameter("pNotDstCod");

    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefNotificacionDestinatarioSWW.jsp?MODO=UPDATE&pNotCod=" + NotCod + "');";

    NotificacionDestinatario destinatario = new NotificacionDestinatario();

    Notificacion notificacion = new Notificacion();
    Retorno_MsgObj retorno = (Retorno_MsgObj) LoNotificacion.GetInstancia().obtener(Long.valueOf(NotCod));

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {

        if (!retorno.SurgioError()) {
            notificacion = (Notificacion) retorno.getObjeto();

            destinatario = notificacion.ObtenerDestinatarioByCod(Long.valueOf(NotDstCod));

        } else {
            out.print(retorno.getMensaje().toString());
        }

    }

    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";

    switch (Mode) {
        case INSERT:
            CamposActivos = "enabled";
            break;
        case DELETE:
            nameButton    = "ELIMINAR";
            nameClass     = "btn-danger";
            break;
        case UPDATE:
            CamposActivos = "enabled";
            nameButton    = "MODIFICAR";
            break;
    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Destinatario</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <script>
            $(document).ready(function () {


                $('#btn_guardar').click(function (event) {
                    if($(this).data("accion") == "<%=Mode.DELETE%>")
                    {
                        $(function () {
                            $('#PopUpConfEliminar').modal('show');
                        });
                    }
                    else
                    {
                        if(validarDatos())
                        {
                            procesarDatos();
                        }
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
                
                

            });
            
            
            function procesarDatos(){

                    var NotCod = $('#NotCod').val();
                    var NotDstCod = $('#NotDstCod').val();
                    var NotEmail = $('#NotEmail').val();
                    var NotPerCod = $('#NotPerCod').val();

                    var Modo = $('#MODO').val();

                    if (NotEmail == '' && NotPerCod == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {



                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<% out.print(urlSistema); %>ABM_NotificacionDestinatario', {
                            pNotDstCod: NotDstCod,
                            pNotEmail: NotEmail,
                            pNotCod: NotCod,
                            pNotPerCod: NotPerCod,
                            pAction: Modo
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                            if (obj.tipoMensaje != 'ERROR')
                            {
            <%
                                    out.print(js_redirect);
            %>
                            }

                        });


                    }
                }

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
                                DESTINATARIO
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionDestinatarioSWW.jsp?MODO=UPDATE&pNotCod=<% out.print(NotCod); %>">Regresar</a>
                            </span>
                        </header>
        
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        
                                                        <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                            <input type="hidden" name="MODO" id="MODO" value="<% out.print(Mode); %>">
                                                            <input type="hidden" name="NotCod" id="NotCod" value="<% out.print(NotCod); %>">
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            <div class="form-group "><label for="NotDstCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="NotDstCod" name="NotDstCod" disabled value="<%=utilidad.NuloToVacio(destinatario.getNotDstCod())%>" ></div></div>
                                                            <div class="form-group "><label for="NotEmail" class="control-label col-lg-3">Email</label><div class="col-lg-6"><input type="email" class=" form-control inputs_generales" id="NotEmail" name="NotEmail" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(destinatario.getNotEmail())%>" ></div></div>
                                                            <div class="form-group ">
                                                                <label for="NotPerCod" class="control-label col-lg-3">Persona</label>
                                                                <div class="col-lg-4">
                                                                    <input type="number" class=" form-control inputs_generales" id="NotPerCod" name="NotPerCod" <%=CamposActivos%> value="<% out.print(utilidad.NuloToVacio((destinatario.getPersona() != null ? destinatario.getPersona().getPerCod() : ""))); %>" >
                                                                </div>
                                                                <div class="col-lg-2">
                                                                    <a href="#" id="btnPerCod" name="btnPerCod" class="glyphicon glyphicon-search" data-toggle="modal" data-target="#PopUpPersona"></a>
                                                                </div>
                                                            </div>
                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input type="button" class="btn <%=nameClass%>" data-accion="<%=Mode%>" name="btn_guardar" id="btn_guardar" value="<%=nameButton%>">
                                                                    <input type="button" class="btn btn-default" onclick="<%=js_redirect%>" value="CANCELAR">
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
        

        <!--Popup Confirmar Eliminación-->
        <div id="PopUpConfEliminar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Eliminar</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <h4>Confirma eliminación?</h4>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button name="btn_conf_eliminar" id="btn_conf_eliminar" class="btn btn-danger" data-dismiss="modal" onclick="procesarDatos()">Eliminar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div> 
        
        <!-- PopUp para Agregar personas del calendario -->

        <div id="PopUpPersona" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content modal-lg">
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

                $(document).ready(function () {

                    Buscar();


                    $(document).on('click', ".PopPer_Seleccionar", function () {

                                              
                        $('#NotPerCod').val($(this).data("codigo"));

                        $(function () {
                            $('#PopUpPersona').modal('toggle');
                        });
                    });


                    function Buscar()
                    {
                        var PerNom = $('#popPerNom').val();

                        $.post('<% out.print(urlSistema); %>ABM_Persona', {
                            popPerNom: PerNom,
                            pAction: "POPUP_OBTENER"
                        }, function (responseText) {


                            var personas = JSON.parse(responseText);

                            $.each(personas, function (f, persona) {

                                persona.perCod = "<td> <a href='#' data-codigo='" + persona.perCod + "' data-nombre='" + persona.perNom + "' class='PopPer_Seleccionar'>" + persona.perCod + " </a> </td>";
                            });

                            $('#PopUpTblPersona').DataTable({
                                data: personas,
                                responsive: true,
                                processing: true,
                                deferRender: true,
                                bLengthChange: false, //thought this line could hide the LengthMenu
                                pageLength: 10,
                                language: {
                                    "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                                }
                                , search: {
                                    "search": "Alumno"
                                }
                                , columns: [
                                    {"data": "perCod"},
                                    {"data": "nombreCompleto"},
                                    {"data": "tipoPersona"},
                                    {"data": "perDoc"}
                                ]

                            });

                        });
                    }


                });
            </script>
        </div>
        
    </body>
</html>