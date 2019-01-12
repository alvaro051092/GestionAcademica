<%-- 
    Document   : DefTipoEvaluacion
    Created on : 03-jul-2017, 18:29:13
    Author     : alvar
--%>

<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Moodle.MoodleCategory"%>
<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
<%@page import="Logica.LoCategoria"%>
<%@page import="Entidad.TipoEvaluacion"%>
<%@page import="Logica.LoTipoEvaluacion"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    LoTipoEvaluacion loTipoEvaluacion = LoTipoEvaluacion.GetInstancia();
    Utilidades utilidad = Utilidades.GetInstancia();

    String urlSistema = utilidad.GetUrlSistema();

    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
    String usuario = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String TpoEvlCod = request.getParameter("pTpoEvlCod");
    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefTipoEvaluacionWW.jsp');";

    TipoEvaluacion tpoEvaluacion = new TipoEvaluacion();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) loTipoEvaluacion.obtener(Long.valueOf(TpoEvlCod));
        if (!retorno.SurgioErrorObjetoRequerido()) {
            tpoEvaluacion = (TipoEvaluacion) retorno.getObjeto();
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

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Tipo Evaluación</title>
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
            
            
            
            function procesarDatos() {


                    var TpoEvlCod = $('#TpoEvlCod').val();
                    var TpoEvlNom = $('#TpoEvlNom').val();
                    var TpoEvlExm = document.getElementById('TpoEvlExm').checked;
                    var TpoEvlInsAut = document.getElementById('TpoEvlInsAut').checked;

                    if (TpoEvlNom == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {

                        if ($('#MODO').val() == "INSERT")
                        {

                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_TipoEvaluacion', {
                                pTpoEvlCod: TpoEvlCod,
                                pTpoEvlNom: TpoEvlNom,
                                pTpoEvlExm: TpoEvlExm,
                                pTpoEvlInsAut: TpoEvlInsAut,
                                pAction: "INSERTAR"
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
            <%
                                                                 out.print(js_redirect);
            %>
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });
                        }


                        if ($('#MODO').val() == "UPDATE")
                        {
                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_TipoEvaluacion', {
                                pTpoEvlCod: TpoEvlCod,
                                pTpoEvlNom: TpoEvlNom,
                                pTpoEvlExm: TpoEvlExm,
                                pTpoEvlInsAut: TpoEvlInsAut,
                                pAction: "ACTUALIZAR"
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
            <%
                                                            out.print(js_redirect);
            %>
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });
                        }

                        if ($('#MODO').val() == "DELETE")
                        {
                            $.post('<% out.print(urlSistema); %>ABM_TipoEvaluacion', {
                                pTpoEvlCod: TpoEvlCod,
                                pTpoEvlNom: TpoEvlNom,
                                pTpoEvlExm: TpoEvlExm,
                                pTpoEvlInsAut: TpoEvlInsAut,
                                pAction: "ELIMINAR"
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
            <%
                                                            out.print(js_redirect);
            %>
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });
                        }
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
                                TIPO DE EVALUACIÓN
                            <!-- BOTONES -->
                            
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Definiciones/DefTipoEvaluacionWW.jsp">Regresar</a>
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
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            
                                                            
                                                            <div class="form-group "><label for="TpoEvlCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="TpoEvlCod" name="TpoEvlCod" disabled value="<%=utilidad.NuloToVacio(tpoEvaluacion.getTpoEvlCod())%>" ></div></div>
                                                            <div class="form-group "><label for="TpoEvlNom" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="TpoEvlNom" name="TpoEvlNom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(tpoEvaluacion.getTpoEvlNom())%>" ></div></div>
                                                            <div class="form-group "><label for="TpoEvlExm" class="control-label col-lg-3">Es exámen</label><div class="col-lg-6"><input type="checkbox"  id="TpoEvlExm" name="TpoEvlExm" <%=CamposActivos%> <%=utilidad.BooleanToChecked(tpoEvaluacion.getTpoEvlExm())%> ></div></div>
                                                            <div class="form-group "><label for="TpoEvlInsAut" class="control-label col-lg-3">Sin plazo de inscripción</label><div class="col-lg-6"><input type="checkbox"  id="TpoEvlInsAut" name="TpoEvlInsAut" <%=CamposActivos%> <%=utilidad.BooleanToChecked(tpoEvaluacion.getTpoEvlInsAut())%> ></div></div>

                                                            
                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input name="btn_guardar" id="btn_guardar"  type="button"  class="btn <%=nameClass%>" data-accion="<%=Mode%>" value="<%=nameButton%>" />
                                                                    <input value="Cancelar" class="btn btn-default" type="button" onclick="<% out.print(js_redirect);%>"/>
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
        
        
   
    </body>
</html>
