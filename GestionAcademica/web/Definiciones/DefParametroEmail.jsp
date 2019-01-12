<%-- 
    Document   : DefVersion
    Created on : 25-jun-2017, 21:43:57
    Author     : alvar
--%>

<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.TipoSSL"%>
<%@page import="Enumerado.TipoAutenticacion"%>
<%@page import="Enumerado.ProtocoloEmail"%>
<%@page import="Entidad.ParametroEmail"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Logica.LoParametroEmail"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    LoParametroEmail lPrmEml = LoParametroEmail.GetInstancia();
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
    String ParEmlCod = request.getParameter("pParEmlCod");
    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefParametroEmailWW.jsp');";

    ParametroEmail paramEml = new ParametroEmail();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        paramEml = lPrmEml.obtener(Long.valueOf(ParEmlCod));
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
        <title>Sistema de Gestión Académica - Versión</title>
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


                    var ParEmlCod = $('#ParEmlCod').val();
                    var ParEmlNom = $('#ParEmlNom').val();
                    var ParEmlPro = $('select[name=ParEmlPro]').val();
                    var ParEmlSrv = $('#ParEmlSrv').val();
                    var ParEmlPrt = $('#ParEmlPrt').val();
                    var ParEmlDeNom = $('#ParEmlDeNom').val();
                    var ParEmlDeEml = $('#ParEmlDeEml').val();
                    var ParEmlUtlAut = document.getElementById('ParEmlUtlAut').checked;
                    var ParEmlTpoAut = $('select[name=ParEmlTpoAut]').val();
                    var ParEmlDom = $('#ParEmlDom').val();
                    var ParEmlUsr = $('#ParEmlUsr').val();
                    var ParEmlPsw = $('#ParEmlPsw').val();
                    var ParEmlSSL = $('select[name=ParEmlSSL]').val();
                    var ParEmlTmpEsp = $('#ParEmlTmpEsp').val();
                    var ParEmlDebug = document.getElementById('ParEmlDebug').checked;
                    var ParEmlReqConf = document.getElementById('ParEmlReqConf').checked;

                    if (ParEmlNom == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {

                        if ($('#MODO').val() == "INSERT")
                        {

                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_ParametroEmail', {
                                pParEmlNom: ParEmlNom,
                                pParEmlPro: ParEmlPro,
                                pParEmlSrv: ParEmlSrv,
                                pParEmlPrt: ParEmlPrt,
                                pParEmlDeNom: ParEmlDeNom,
                                pParEmlDeEml: ParEmlDeEml,
                                pParEmlUtlAut: ParEmlUtlAut,
                                pParEmlTpoAut: ParEmlTpoAut,
                                pParEmlDom: ParEmlDom,
                                pParEmlUsr: ParEmlUsr,
                                pParEmlPsw: ParEmlPsw,
                                pParEmlSSL: ParEmlSSL,
                                pParEmlTmpEsp: ParEmlTmpEsp,
                                pParEmlDebug: ParEmlDebug,
                                pParEmlReqConf: ParEmlReqConf,
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
                            $.post('<% out.print(urlSistema); %>ABM_ParametroEmail', {
                                pParEmlCod: ParEmlCod,
                                pParEmlNom: ParEmlNom,
                                pParEmlPro: ParEmlPro,
                                pParEmlSrv: ParEmlSrv,
                                pParEmlPrt: ParEmlPrt,
                                pParEmlDeNom: ParEmlDeNom,
                                pParEmlDeEml: ParEmlDeEml,
                                pParEmlUtlAut: ParEmlUtlAut,
                                pParEmlTpoAut: ParEmlTpoAut,
                                pParEmlDom: ParEmlDom,
                                pParEmlUsr: ParEmlUsr,
                                pParEmlPsw: ParEmlPsw,
                                pParEmlSSL: ParEmlSSL,
                                pParEmlTmpEsp: ParEmlTmpEsp,
                                pParEmlDebug: ParEmlDebug,
                                pParEmlReqConf: ParEmlReqConf,
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
                            $.post('<% out.print(urlSistema); %>ABM_ParametroEmail', {
                                pParEmlCod: ParEmlCod,
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
                                PARAMETRO EMAIL
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
                                                        
                                                        <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                            <input type="hidden" name="MODO" id="MODO" value="<% out.print(Mode); %>">
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            
                                                            <div class="form-group "><label for="ParEmlCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="ParEmlCod" name="ParEmlCod" disabled value="<%=utilidad.NuloToVacio(paramEml.getParEmlCod())%>" ></div></div>
                                                            <div class="form-group "><label for="ParEmlNom" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="ParEmlNom" name="ParEmlNom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlNom())%>" ></div></div>

                                                            <div class="form-group ">
                                                                <label for="ParEmlPro" class="control-label col-lg-3">Protocolo</label>
                                                                <div class="col-lg-6">
                                                                    <select class="form-control inputs_generales" id="ParEmlPro" name="ParEmlPro" <% out.print(CamposActivos); %>>
                                                                        <%
                                                                                for (ProtocoloEmail protocolo : ProtocoloEmail.values()) {
                                                                                        if (protocolo == paramEml.getParEmlPro()) {
                                                                                                out.println("<option selected value='" + protocolo.getCod() + "'>" + protocolo.getNom() + "</option>");
                                                                                        } else {
                                                                                                out.println("<option value='" + protocolo.getCod() + "'>" + protocolo.getNom() + "</option>");
                                                                                        }
                                                                                }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                                    
                                                            <div class="form-group "><label for="ParEmlSrv" class="control-label col-lg-3">Servidor de correo</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="ParEmlSrv" name="ParEmlSrv" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlSrv())%>" ></div></div>
                                                            <div class="form-group "><label for="ParEmlPrt" class="control-label col-lg-3">Puerto</label><div class="col-lg-6"><input type="number" required class=" form-control inputs_generales" id="ParEmlPrt" name="ParEmlPrt" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlPrt())%>" ></div></div>
                                                            <div class="form-group "><label for="ParEmlDom" class="control-label col-lg-3">Dominio</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="ParEmlDom" name="ParEmlDom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlDom())%>" ></div></div>
                                                          
                                                            <div class="form-group "><label for="ParEmlDeNom" class="control-label col-lg-3">De nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="ParEmlDeNom" name="ParEmlDeNom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlDeNom())%>" ></div></div>
                                                            <div class="form-group "><label for="ParEmlDeEml" class="control-label col-lg-3">De email</label><div class="col-lg-6"><input type="email" required class=" form-control inputs_generales" id="ParEmlDeEml" name="ParEmlDeEml" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlDeEml())%>" ></div></div>
                                                            
                                                            <div class="form-group ">
                                                                <label for="ParEmlTpoAut" class="control-label col-lg-3">Tipo de autenticación</label>
                                                                <div class="col-lg-6">
                                                                    <select class="form-control inputs_generales" id="ParEmlTpoAut" name="ParEmlTpoAut" <% out.print(CamposActivos); %>>
                                                                        <%
                                                                            for (TipoAutenticacion tpoAut : TipoAutenticacion.values()) {
                                                                                if (tpoAut == paramEml.getParEmlTpoAut()) {
                                                                                        out.println("<option selected value='" + tpoAut.getCod() + "'>" + tpoAut.getNom() + "</option>");
                                                                                } else {
                                                                                        out.println("<option value='" + tpoAut.getCod() + "'>" + tpoAut.getNom() + "</option>");
                                                                                }
                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                                        
                                                            <div class="form-group "><label for="ParEmlUsr" class="control-label col-lg-3">Usuario</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="ParEmlUsr" name="ParEmlUsr" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlUsr())%>" ></div></div>
                                                            <div class="form-group "><label for="ParEmlPsw" class="control-label col-lg-3">Contraseña</label><div class="col-lg-6"><input type="password" required class=" form-control inputs_generales" id="ParEmlPsw" name="ParEmlPsw" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(paramEml.getParEmlPsw())%>" ></div></div>

                                                            <div class="form-group ">
                                                                <label for="ParEmlSSL" class="control-label col-lg-3">SSL</label>
                                                                <div class="col-lg-6">
                                                                    
                                                                    <select class="form-control inputs_generales" id="ParEmlSSL" name="ParEmlSSL" <% out.print(CamposActivos); %>>
                                                                            <%
                                                                                for (TipoSSL tpoSSL : TipoSSL.values()) {
                                                                                    if (tpoSSL == paramEml.getParEmlSSL()) {
                                                                                            out.println("<option selected value='" + tpoSSL.getCod() + "'>" + tpoSSL.getNom() + "</option>");
                                                                                    } else {
                                                                                            out.println("<option value='" + tpoSSL.getCod() + "'>" + tpoSSL.getNom() + "</option>");
                                                                                    }
                                                                                }
                                                                            %>
                                                                    </select>
                                                                    
                                                                </div>
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="ParEmlTmpEsp" class="control-label col-lg-3">Tiempo de espera en segundos</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="ParEmlTmpEsp" name="ParEmlTmpEsp" <%=CamposActivos%> value="<%=utilidad.NuloToCero(paramEml.getParEmlTmpEsp())%>" ></div></div>
                                                            

                                                            <div class="form-group "><label for="ParEmlUtlAut" class="control-label col-lg-3">Utiliza autenticación</label><div class="col-lg-6"><input type="checkbox" id="ParEmlUtlAut" name="ParEmlUtlAut" <%=CamposActivos%> <%=utilidad.BooleanToChecked(paramEml.getParEmlUtlAut())%> ></div></div>
                                                            <div class="form-group "><label for="ParEmlReqConf" class="control-label col-lg-3">Requiere confirmación</label><div class="col-lg-6"><input type="checkbox" id="ParEmlReqConf" name="ParEmlReqConf" <%=CamposActivos%> <%=utilidad.BooleanToChecked(paramEml.getParEmlReqConf())%> ></div></div>

                                                            
                                                            <div class="form-group "><label for="ParEmlDebug" class="control-label col-lg-3">Debug</label><div class="col-lg-6"><input type="checkbox"  id="ParEmlDebug" name="ParEmlDebug" <%=CamposActivos%> <%=utilidad.BooleanToChecked(paramEml.getParEmlDebug())%> ></div></div>
                                                            
                                                            
                                                                                                                        
                                                            
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
