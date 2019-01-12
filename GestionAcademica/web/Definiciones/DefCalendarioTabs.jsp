<%-- 
    Document   : DefCalendarioTabs
    Created on : 03-jul-2017, 18:29:23
    Author     : alvar
--%>

<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoCalendario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<header class="panel-heading tab-bg-dark-navy-blue ">
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();
    
    Modo Mode           = Modo.valueOf(request.getParameter("MODO"));
    String CalCod       = request.getParameter("pCalCod");
    
    String ret = request.getParameter("RET");
    String urlRet = urlSistema + "Definiciones/DefCalendarioWW.jsp";
    if (ret != null) {
        if (!ret.isEmpty()) {
            urlRet = urlSistema + "Definiciones/DefCalendarioGrid.jsp";
        }
    }
   
    String urlActual = utilidad.GetPaginaActual(request);
    
    out.println("<ul class='nav nav-tabs'>");
    
    out.println("<li class='" + (urlActual.equals("DefCalendario.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefCalendario.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefCalendario.jsp?MODO="+Mode+"&pCalCod=" +CalCod+"'>Calendario</a></li>");
    if(!Mode.equals(Modo.INSERT)) out.println("<li class='" + (urlActual.equals("DefCalendarioAlumnoSWW.jsp") ? "active" : "")+"'><a  class='tabs' aria-expanded='"+(urlActual.equals("DefCalendarioAlumnoSWW.jsp") ? "true" : "false")+"'  href='" + urlSistema + "Definiciones/DefCalendarioAlumnoSWW.jsp?MODO=" +Mode + "&pCalCod=" + CalCod + "'>Alumnos</a></li>");
    if(!Mode.equals(Modo.INSERT)) out.println("<li class='" + (urlActual.equals("DefCalendarioDocenteSWW.jsp") ? "active" : "")+"'><a  class='tabs' aria-expanded='"+(urlActual.equals("DefCalendarioDocenteSWW.jsp") ? "true" : "false")+"'  href='" + urlSistema + "Definiciones/DefCalendarioDocenteSWW.jsp?MODO=" +Mode + "&pCalCod=" + CalCod + "'>Docentes</a></li>");
    
    out.println("</ul>");
%>
    
    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar" href="<% out.print(urlRet); %>">Regresar</a>
        </div>
    </span>
        
</header>