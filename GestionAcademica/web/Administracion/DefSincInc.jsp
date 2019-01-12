<%-- 
    Document   : DefSincInc
    Created on : 18-ago-2017, 10:07:37
    Author     : alvar
--%>

<%@page import="Entidad.SincInconsistenciaDatos"%>
<%@page import="Entidad.SincronizacionInconsistencia"%>
<%@page import="Logica.LoSincronizacion"%>
<%@page import="Entidad.Sincronizacion"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>
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
    String SncCod = request.getParameter("pSncCod");
    String IncCod = request.getParameter("pIncCod");
    String js_redirect = "window.location.replace('" + urlSistema + "Administracion/DefSincIncSWW.jsp?MODO=" + Enumerado.Modo.UPDATE + "&pSncCod=" + SncCod + "');";

    SincronizacionInconsistencia inc = new SincronizacionInconsistencia();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoSincronizacion.GetInstancia().obtener(Long.valueOf(SncCod));
        if (!retorno.SurgioError()) {
            Sincronizacion sinc = (Sincronizacion) retorno.getObjeto();
            inc = sinc.GetInconsistencia(Long.valueOf(IncCod));
        } else {
            out.print(retorno.getMensaje().toString());
        }

    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Inconsistencia</title>
        <jsp:include page="/masterPage/head.jsp"/>
        
        <link href="<% out.print(urlSistema); %>JavaScript/JsonViewer/jquery.json-viewer.css" rel="stylesheet">
        <script src="<% out.print(urlSistema); %>JavaScript/JsonViewer/jquery.json-viewer.js"></script>
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
                        <header class="panel-heading">
                            <!-- TITULO -->
                            INCONSISTENCIA
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Administracion/DefSincIncSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pSncCod=<% out.print(SncCod); %>">Regresar</a>
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
                                                        <div name="datos_ocultos">
                                                        <input type="hidden" name="MODO" id="MODO" value="<% out.print(Mode); %>">
                                                        </div>
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                        <!-- CONTENIDO -->
                                                        
                                                            <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                                <input type="hidden" name="MODO" id="MODO" value="<%=Mode%>">
                                                                <input type="hidden" id="SncCod" name="SncCod" value="<%=SncCod%>">
                                                                <input type="hidden" id="IncCod" name="IncCod" value="<%=IncCod%>">
                                                                <input type="hidden" id="IncObjCod" name="IncObjCod" value="">
                                                            </div>

                                                            <div>
                                                                <div>Código: <label><% out.print(inc.getIncCod()); %></label></div>
                                                                <div>Registro: <label><% out.print(inc.getObjeto().getObjNom()); %></label></div>
                                                                <div>Estado: <label><% out.print(inc.getIncEst()); %></label></div>
                                                                <div>Objeto valido: <label id="lbl_IncObjCod"><% out.print((inc.getObjetoSeleccionado() == null ? "" : inc.getObjetoSeleccionado().getIncObjCod())); %></label></div>

                                                                <label>Objetos</label>                            
                                                                <div class="row">
                                                                    <%
                                                                        for(SincInconsistenciaDatos dat : inc.getLstDatos())
                                                                        {
                                                                            String dato = "<div class='col-lg-6'>"
                                                                                    + "<pre id='jquery_viewer_"+dat.getIncObjCod()+"'></pre>"
                                                                                    + "<input type='hidden' name='jquery_data_"+dat.getIncObjCod()+"' id='jquery_data_"+dat.getIncObjCod()+"' class='jquery_data' data-value='"+dat.getObjVal()+"' data-id='"+dat.getIncObjCod()+"'/>"
                                                                                    + "<input type='button' data-id='"+dat.getIncObjCod()+"' class='btn btn-default jquery_select' value='Seleccionar'/>"
                                                                                    + "</div>";

                                                                            out.println(dato);
                                                                        }
                                                                    %>

                                                                </div>
                                                                    
                                                                    <div class="form-group">
                                                                        <div class="col-lg-offset-3 col-lg-6">
                                                                            <input name="btn_guardar" id="btn_guardar"  type="button"  class="btn btn-primary"  value="CONFIRMAR" />
                                                                            <input value="Cancelar" class="btn btn-default" type="button" onclick="<% out.print(js_redirect);%>"/>
                                                                        </div>
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
        
      
        <script>
            $(document).ready(function () {
                    $('input[class="jquery_data"]').each(function() {
                        // `this` is the div
        
        
                        var jsonValor = $(this).data('value');

                        //var jsonValor = $(this).val();
                        var codigoPre = "#jquery_viewer_" + $(this).data("id");
                        
                        $(codigoPre).jsonViewer(jsonValor);

                    });
                    
                    $('.jquery_select').on('click', function (e) {
                        $('#lbl_IncObjCod').text($(this).data("id"));
                        $('#IncObjCod').val($(this).data("id"));
                    });
                    
                    $('#btn_guardar').click(function (event) {


                        var SncCod = $('#SncCod').val();
                        var IncCod = $('#IncCod').val();
                        var IncObjCod = $('#IncObjCod').val();

                        if (IncObjCod == '')
                        {
                            MostrarMensaje("ERROR", "Completa los datos papa");
                        } else
                        {


                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_Sincronizacion', {
                                pSncCod: SncCod,
                                pIncCod: IncCod,
                                pIncObjCod: IncObjCod,
                                pAction: 'INC_SELECCIONAR'
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
                    });
                });
        </script>
    </body>
</html>
