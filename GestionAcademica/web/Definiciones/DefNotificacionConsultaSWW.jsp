<%-- 
    Document   : DefCalendarioWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Entidad.NotificacionConsulta"%>
<%@page import="Entidad.NotificacionConsulta"%>
<%@page import="Entidad.Notificacion"%>
<%@page import="Logica.LoNotificacion"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.Calendario"%>
<%@page import="java.util.List"%>
<%@page import="Logica.LoCalendario"%>
<%@page import="Utiles.Utilidades"%>
<%

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
    String NotCod = request.getParameter("pNotCod");
    
    String titulo = "";

    List<NotificacionConsulta> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) LoNotificacion.GetInstancia().obtener(Long.valueOf(NotCod));
    if (!retorno.SurgioError()) {
        Notificacion notificacion = (Notificacion) retorno.getObjeto();
        lstObjeto = notificacion.getLstConsulta();
        titulo = notificacion.getNotNom();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Notificación <%=titulo%> | Consultas</title>
        <jsp:include page="/masterPage/head.jsp"/>
        <jsp:include page="/masterPage/head_tables.jsp"/>
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
                        <jsp:include page="/Definiciones/DefNotificacionTabs.jsp"/>
                        
                        <div class="contenedor_agregar">
                            <a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionConsulta.jsp?MODO=<% out.print(Enumerado.Modo.INSERT); %>&pNotCod=<% out.print(NotCod); %>" title="Ingresar" class="glyphicon glyphicon-plus"> </a>
                        </div>
                        
			<div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <div name="datos_ocultos">
                                                            <input type="hidden" name="NotCod" id="NotCod" value="<% out.print(NotCod); %>">
                                                        </div>
                                                        
                                                        <table id='tbl_ww' style=' <% out.print(tblVisible); %>' class='table table-hover'>
                                                            <thead>
                                                                <tr>
                                                                    <th></th>
                                                                    <th></th>
                                                                    <th>Código</th>
                                                                    <th>Tipo</th>
                                                                    <th>Query</th>
                                                                </tr>
                                                            </thead>

                                                            <% for (NotificacionConsulta consulta : lstObjeto) {

                                                            %>
                                                            <tr>
                                                                <td><a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionConsulta.jsp?MODO=<% out.print(Enumerado.Modo.DELETE); %>&pNotCod=<% out.print(consulta.getNotificacion().getNotCod()); %>&pNotCnsCod=<% out.print(consulta.getNotCnsCod()); %>" name="btn_eliminar" id="btn_eliminar" title="Eliminar" class="glyphicon glyphicon-trash"/></td>
                                                                <td><a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionConsulta.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pNotCod=<% out.print(consulta.getNotificacion().getNotCod()); %>&pNotCnsCod=<% out.print(consulta.getNotCnsCod()); %>" name="btn_editar" id="btn_editar" title="Editar" class='glyphicon glyphicon-edit'/></td>
                                                                <td><% out.print(utilidad.NuloToVacio(consulta.getNotCnsCod())); %> </td>
                                                                <td><% out.print(utilidad.NuloToVacio(consulta.getNotCnsTpo().getNombre())); %> </td>
                                                                <td><% out.print(utilidad.NuloToVacio(consulta.getNotCnsSQL())); %> </td>

                                                            </tr>
                                                            <%
                                                                }
                                                            %>
                                                        </table>
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
    </body>
</html>
