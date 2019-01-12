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
    
    String mostrarTabs     = request.getParameter("MostrarTabs");
    String PeriEstCod      = request.getParameter("Codigo");
   
    String urlActual = utilidad.GetPaginaActual(request);
   
    out.println("<ul class='nav nav-tabs'>");
    
    if(mostrarTabs.equals("SI")) out.println("<li class='" + (urlActual.equals("DefPeriodoAlumnoSWW.jsp") ? "active" : "")+"'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPeriodoAlumnoSWW.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefPeriodoAlumnoSWW.jsp?MODO=" +Modo.UPDATE + "&pPeriEstCod=" + PeriEstCod + "'>Alumnos</a></li>");
    if(mostrarTabs.equals("SI")) out.println("<li class='" + (urlActual.equals("DefPeriodoDocenteSWW.jsp") ? "active" : "")+"'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPeriodoDocenteSWW.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefPeriodoDocenteSWW.jsp?MODO=" +Modo.UPDATE + "&pPeriEstCod=" + PeriEstCod + "'>Docentes</a></li>");
    if(mostrarTabs.equals("SI")) out.println("<li class='" + (urlActual.equals("DefPeriodoDocumentoSWW.jsp") ? "active" : "")+"'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPeriodoDocumentoSWW.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefPeriodoDocumentoSWW.jsp?MODO=" +Modo.UPDATE+ "&pPeriEstCod=" + PeriEstCod + "'>Documentos</a></li>");
    out.println("</ul>");
%>
    
    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar"  href="<% out.print(urlSistema); %>Definiciones/DefPeriodoWW.jsp">Regresar</a>
        </div>
    </span>
</header>