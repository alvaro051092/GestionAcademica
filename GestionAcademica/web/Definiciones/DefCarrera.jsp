<%-- 
    Document   : DefCarrera
    Created on : jun 21, 2017, 7:51:06 p.m.
    Author     : aa
--%>

<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.LoCarrera"%>
<%@page import="Entidad.Carrera"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<%
    Utilidades utilidad = Utilidades.GetInstancia();
    LoCarrera loCar = LoCarrera.GetInstancia();
    
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
    String CarCod = request.getParameter("pCarCod");
    Modo mode = Modo.valueOf(request.getParameter("MODO"));
    String js_redirect = "location.replace('" + urlSistema + "Definiciones/DefCarreraWW.jsp');";
    
    Carrera car = new Carrera();

    if (mode.equals(Modo.UPDATE) || mode.equals(Modo.DISPLAY) || mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) loCar.obtener(Long.valueOf(CarCod));
        if (retorno.getMensaje().getTipoMensaje() != TipoMensaje.ERROR) {
            car = (Carrera) retorno.getObjeto();
        } else {
            out.print(retorno.getMensaje().toString());
        }
    }
    
    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";
    
    switch (mode) {
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

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Carrera</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <script>
            $(document).ready(function () 
            {
                $('#btn_guardar').click(function ()
                {
                    if($(this).data("accion") == "<%=mode.DELETE%>")
                    {
                        $(function () 
                        {
                            $('#PopUpConfEliminar').modal('show');
                        })
                    }
                    else
                    {
                        if(validarDatos())
                        {
                            procesarDatos();
                        }
                    }
                });
                
                $('#btn_conf_eliminar').click(function()
                {
                    procesarDatos();
                });
                
                function validarDatos()
                {    
                    if(!$('#frm_general')[0].checkValidity())
                    {
                        var $myForm = $('#frm_general');
                        $myForm.find(':submit').click();
                        return false;
                    }
                    return true;
                }
                
                function procesarDatos()
                {
                    var codVar = $('#CarCod').val();
                    var nomVar = $('#CarNom').val();
                    var dscVar = $('#CarDsc').val();
                    var facVar = $('#CarFac').val();
                    var crtVar = $('#CarCrt').val();

                    var modo = $('#MODO').val();

                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<%=urlSistema%>ABM_Carrera', 
                    {
                        pCod: codVar,
                        pNom: nomVar,
                        pDsc: dscVar,
                        pfac: facVar,
                        pCrt: crtVar,

                        pAction: modo
                    }
                    , function (responseText) 
                    {
                        var obj = JSON.parse(responseText);

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                        {
                            <%=js_redirect%>
                        } 

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
                            <jsp:include page="/Definiciones/DefCarreraTabs.jsp"/>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >

                                                            <!-- DATOS PERSONALES -->
                                                            <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                                <input type="hidden" name="MODO" id="MODO" value="<% out.print(mode); %>">
                                                            </div>

                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="CarCod" name="CarCod" disabled value="<%=utilidad.NuloToVacio(car.getCarCod())%>" ></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="CarNom" name="CarNom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(car.getCarNom())%>" ></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Descripción</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="CarDsc" name="CarDsc" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(car.getCarDsc())%>" ></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Facultad</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="CarFac" name="CarFac" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(car.getCarFac())%>" ></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Certificación</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="CarCrt" name="CarCrt" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(car.getCarCrt())%>" ></div></div>

                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input type="button" class="btn <%=nameClass%>" data-accion="<%=mode%>" name="btn_guardar" id="btn_guardar" value="<%=nameButton%>">
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
    </body>
</html>