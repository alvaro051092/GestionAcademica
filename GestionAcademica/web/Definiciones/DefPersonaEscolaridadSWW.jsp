<%-- 
    Document   : DefPersonaEscolaridad
    Created on : 30-jun-2017, 20:51:03
    Author     : alvar
--%>

<%@page import="Entidad.Escolaridad"%>
<%@page import="SDT.SDT_PersonaEstudio"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Persona"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Utilidades utilidad = Utilidades.GetInstancia();

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
    String PerCod = request.getParameter("pPerCod");

    ArrayList<SDT_PersonaEstudio> lstEstudio = new ArrayList<>();
    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        lstEstudio = LoPersona.GetInstancia().ObtenerEstudios(Long.valueOf(PerCod));
    }

    String tblVisible = (lstEstudio.size() > 0 ? "" : "display: none;");

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Persona | Escolaridad</title>
        <jsp:include page="/masterPage/head.jsp"/>
    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <jsp:include page="/masterPage/cabezal_menu.jsp"/>
        
        
        <!-- CONTENIDO -->
        <div class="contenido" id="contenedor">

            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                            <jsp:include page="/Definiciones/DefPersonaTabs.jsp"/>
                        <div class="panel-body">
                            <div class=" form">
                                
                                <div name="cont_estudio" class="col-sm-8" style=' <% out.print(tblVisible); %>'>
                                    <%
                                        for (SDT_PersonaEstudio est : lstEstudio) {

                                            if (est.getInscripcion().getInsCod() == Long.valueOf("0")) {
                                                out.println("<div class='contenedor_titulo_escolaridad'><label>Sin inscripción</label></div>");
                                            } else {
                                                out.println("<div class='contenedor_titulo_escolaridad'><label>Inscripto a: " + est.getInscripcion().getNombreEstudio() + "</label></div>");
                                            }

                                            out.println("<div class='contenedor_tabla_escolaridad'>");
                                            out.println("<table class='table table-hover eliminar_margen_tabla'>");
                                            out.println("<thead><tr>");
                                            out.println("<th>Materia</th>");
                                            out.println("<th>Fecha</th>");
                                            out.println("<th class='texto_derecha'>Curso</th>");
                                            out.println("<th class='texto_derecha'>Examen</th>");
                                            out.println("<th class='texto_derecha'>Estado</th>");
                                            out.println("</tr>");
                                            out.println("</thead>");
                                            out.println("<tbody>");

                                            for (Escolaridad esc : est.getEscolaridad()) {
                                                out.println("<tr>");

                                                out.println("<td>");
                                                out.println(esc.getNombreEstudio());
                                                out.println("</td>");

                                                out.println("<td>");
                                                out.println(utilidad.NuloToVacio(esc.getEscFch()));
                                                out.println("</td>");

                                                out.println("<td class='texto_derecha'>");
                                                out.println("<label>" + (esc.Revalida() ? "0" : utilidad.NuloToCero(esc.getEscCurVal())) + "</label>");
                                                out.println("</td>");

                                                out.println("<td class='texto_derecha'>");
                                                out.println("<label>" + (esc.Revalida() ? "0" : utilidad.NuloToCero(esc.getEscCalVal())) + "</label>");
                                                out.println("</td>");

                                                out.println("<td class='texto_derecha'>");
                                                out.println("<label>" + esc.getAprobacion() + "</label>");
                                                out.println("</td>");

                                                out.println("</tr>");
                                            }

                                            out.println("</tbody>");
                                            out.println("</table>");

                                            out.println("</div>");
                                        }
                                    %>
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