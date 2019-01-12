<%-- 
    Document   : DefModulo
    Created on : 04-jul-2017, 19:31:27
    Author     : alvar
--%>

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
    LoCurso loCurso = LoCurso.GetInstancia();
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
    String CurCod = request.getParameter("pCurCod");
    String ModCod = request.getParameter("pModCod");
    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefCursoModuloSWW.jsp?MODO=UPDATE&pCurCod=" + CurCod + "');";

    Curso curso = new Curso();
    Retorno_MsgObj retorno = (Retorno_MsgObj) loCurso.obtener(Long.valueOf(CurCod));
    if (retorno.getMensaje().getTipoMensaje() != TipoMensaje.ERROR) {
        curso = (Curso) retorno.getObjeto();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    Modulo modulo = new Modulo();
    modulo.setCurso(curso);

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        modulo = curso.getModuloById(Long.valueOf(ModCod));
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
        <title>Sistema de Gestión Académica - Modulo</title>
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


                    var CurCod = $('#CurCod').val();
                    var ModCod = $('#ModCod').val();
                    var ModNom = $('#ModNom').val();
                    var ModDsc = $('#ModDsc').val();
                    var ModTpoPer = $('select[name=ModTpoPer]').val();
                    var ModPerVal = $('#ModPerVal').val();
                    var ModCntHor = $('#ModCntHor').val();

                    if (ModNom == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {

                        if ($('#MODO').val() == "INSERT")
                        {

                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_Modulo', {
                                pCurCod: CurCod,
                                pModCod: ModCod,
                                pModNom: ModNom,
                                pModDsc: ModDsc,
                                pModTpoPer: ModTpoPer,
                                pModPerVal: ModPerVal,
                                pModCntHor: ModCntHor,
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
                            $.post('<% out.print(urlSistema); %>ABM_Modulo', {
                                pCurCod: CurCod,
                                pModCod: ModCod,
                                pModNom: ModNom,
                                pModDsc: ModDsc,
                                pModTpoPer: ModTpoPer,
                                pModPerVal: ModPerVal,
                                pModCntHor: ModCntHor,
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
                            $.post('<% out.print(urlSistema); %>ABM_Modulo', {
                                pCurCod: CurCod,
                                pModCod: ModCod,
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
                        <!-- TABS -->
                        <jsp:include page="/Definiciones/DefModuloTabs.jsp"/>
			<div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <div name="datos_ocultos">
                                                            <input type="hidden" name="MODO" id="MODO" value="<% out.print(Mode); %>">
                                                            <input type="hidden" name="CurCod" id="CurCod" value="<% out.print(curso.getCurCod()); %>">
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            
                                                            <div class="form-group "><label for="ModCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="ModCod" name="ModCod" disabled value="<%=utilidad.NuloToVacio(modulo.getModCod())%>" ></div></div>
                                                            <div class="form-group "><label for="ModNom" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="ModNom" name="ModNom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(modulo.getModNom())%>" ></div></div>
                                                            <div class="form-group "><label for="ModDsc" class="control-label col-lg-3">Descripcion</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="ModDsc" name="ModDsc" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(modulo.getModDsc())%>" ></div></div>
                                                            <div class="form-group "><label for="ModCntHor" class="control-label col-lg-3">Carga horaria</label><div class="col-lg-6"><input type="number" step="0.1" class=" form-control inputs_generales" id="ModCntHor" name="ModCntHor" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(modulo.getModCntHor())%>" ></div></div>
                                                            
                                                            <div class="form-group ">
                                                                <label for="ModPerVal" class="control-label col-lg-3">Periodo</label>
                                                                <div class="col-lg-1">
                                                                    <input type="number" step="0.1" required class=" form-control inputs_generales" id="ModPerVal" name="ModPerVal" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(modulo.getModPerVal())%>" >
                                                                </div>
                                                                <div class="col-lg-5">
                                                                    <select class="form-control inputs_generales" id="ModTpoPer" name="ModTpoPer" <% out.print(CamposActivos); %>>
                                                                        <%
                                                                            for (TipoPeriodo tpoPeriodo : TipoPeriodo.values()) {
                                                                                
                                                                                out.println("<option " + (modulo.getModTpoPer() == tpoPeriodo ? "selected" : "") + " value='" + tpoPeriodo.getTipoPeriodo() + "'>" + tpoPeriodo.getTipoPeriodoNombre() + "</option>");

                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            
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