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
        String PeriCod      = request.getParameter("pPeriCod");

        String urlActual = utilidad.GetPaginaActual(request);

        out.println("<ul class='nav nav-tabs'>");

        out.println("<li class='" + (urlActual.equals("DefPeriodo.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPeriodo.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefPeriodo.jsp?MODO="+Mode+"&pPeriCod=" +PeriCod+"'>Periodo</a></li>");
        if(!Mode.equals(Modo.INSERT)) out.println("<li class='" + (urlActual.equals("DefPeriodoEstudioSWW.jsp") ? "active" : "")+"'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPeriodoEstudioSWW.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefPeriodoEstudioSWW.jsp?MODO=" +Mode + "&pPeriCod=" + PeriCod + "'>Estudios</a></li>");
        out.println("</ul>");
    %>

    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar"  href="<% out.print(urlSistema); %>Definiciones/DefPeriodoWW.jsp">Regresar</a>
        </div>
    </span>
</header>