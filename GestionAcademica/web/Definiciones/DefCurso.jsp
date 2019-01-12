<%-- 
    Document   : DefCurso
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
<%@page import="Entidad.Curso"%>
<%@page import="Logica.LoCurso"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    LoParametro loParam = LoParametro.GetInstancia();
    Parametro param = loParam.obtener();
    LoCategoria loCat = LoCategoria.GetInstancia();
    LoCurso loCurso = LoCurso.GetInstancia();
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
    String CurCod = request.getParameter("pCurCod");
    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefCursoWW.jsp');";

    Curso curso = new Curso();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) loCurso.obtener(Long.valueOf(CurCod));
        if (retorno.getMensaje().getTipoMensaje() != TipoMensaje.ERROR) {
            curso = (Curso) retorno.getObjeto();
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

    MoodleCategory[] lstCategorias = null;
    if (param.getParUtlMdl()) {
        lstCategorias = loCat.Mdl_ObtenerListaCategorias();
    }

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Persona</title>
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
                    var CurNom = $('#CurNom').val();
                    var CurDsc = $('#CurDsc').val();
                    var CurFac = $('#CurFac').val();
                    var CurCrt = $('#CurCrt').val();
                    var CurCatCod = $('select[name=CurCatCod]').val();

                    if (CurNom == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {

                        if ($('#MODO').val() == "INSERT")
                        {

                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_Curso', {
                                pCurCod: CurCod,
                                pCurNom: CurNom,
                                pCurDsc: CurDsc,
                                pCurFac: CurFac,
                                pCurCrt: CurCrt,
                                pCurCatCod: CurCatCod,
                                pAction: "INSERTAR"
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    <%=js_redirect%>
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });
                        }


                        if ($('#MODO').val() == "UPDATE")
                        {
                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_Curso', {
                                pCurCod: CurCod,
                                pCurNom: CurNom,
                                pCurDsc: CurDsc,
                                pCurFac: CurFac,
                                pCurCrt: CurCrt,
                                pCurCatCod: CurCatCod,
                                pAction: "ACTUALIZAR"
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    <%=js_redirect%>
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });
                        }

                        if ($('#MODO').val() == "DELETE")
                        {
                            $.post('<% out.print(urlSistema); %>ABM_Curso', {
                                pCurCod: CurCod,
                                pAction: "ELIMINAR"
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    <%=js_redirect%>
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
                        <jsp:include page="/Definiciones/DefCursoTabs.jsp"/>
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
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                        <!-- CONTENIDO -->
                                                            <div class="form-group "><label for="CurCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="CurCod" name="CurCod" disabled value="<%=utilidad.NuloToVacio(curso.getCurCod())%>" ></div></div>
                                                            <div class="form-group "><label for="CurNom" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="CurNom" name="CurNom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(curso.getCurNom())%>" ></div></div>
                                                            <div class="form-group "><label for="CurDsc" class="control-label col-lg-3">Descripción</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="CurDsc" name="CurDsc" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(curso.getCurDsc())%>" ></div></div>
                                                            <div class="form-group "><label for="CurFac" class="control-label col-lg-3">Facultad</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="CurFac" name="CurFac" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(curso.getCurFac())%>" ></div></div>
                                                            <div class="form-group "><label for="CurCrt" class="control-label col-lg-3">Certificación</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="CurCrt" name="CurCrt" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(curso.getCurCrt())%>" ></div></div>
                                                            
                                                            <%
                                                                if (param.getParUtlMdl()) {
                                                            %>
                                                            <div class="form-group ">
                                                                <label for="CurCatCod" class="control-label col-lg-3">Categoría (Moodle)</label>
                                                                <div class="col-lg-6">
                                                                    <select class="form-control inputs_generales" id="CurCatCod" name="CurCatCod" <%=CamposActivos%>>
                                                                        <option value=''>Nueva</option>
                                                                        <%
                                                                            if(lstCategorias != null)
                                                                            {
                                                                                for (int i = 0; i < lstCategorias.length; i++) {
                                                                                    MoodleCategory mdlCat = lstCategorias[i];

                                                                                    out.println("<option " + (mdlCat.getId() == curso.getCurCatCod() ? "selected" : "") + " value='" + mdlCat.getId() + "'>" + mdlCat.getName() + "</option>");

                                                                                }
                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <%
                                                                }
                                                            %>
                                                            
                                                            
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
