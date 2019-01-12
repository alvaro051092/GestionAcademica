<%-- 
    Document   : DefPlanEstudioTabs
    Created on : jul 21, 2017, 2:55:15 a.m.
    Author     : aa
--%>

<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();
    
    String urlActual    = Utilidades.GetInstancia().GetPaginaActual(request);
    Modo Mode           = Modo.valueOf(request.getParameter("MODO"));
    String CarCod       = request.getParameter("pCarCod");
    String PlaEstCod    = request.getParameter("pPlaEstCod");
    
    
%>
    
<header class="panel-heading tab-bg-dark-navy-blue ">
    <ul class="nav nav-tabs">
        <% 
            out.println("<li class='" + (urlActual.equals("DefPlanEstudio.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPlanEstudio.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefPlanEstudio.jsp?MODO=" + Mode + "&pCarCod=" + CarCod + "&pPlaEstCod=" + PlaEstCod + "'>Plan</a></li>");
            
            if(!Mode.equals(Mode.INSERT)) out.println("<li class='" + (urlActual.equals("DefMateriaSWW.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefMateriaSWW.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefMateriaSWW.jsp?MODO=" + Mode + "&pCarCod=" + CarCod + "&pPlaEstCod=" + PlaEstCod + "'>Materias</a></li>");
        %>
    </ul>
    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar" href="<% out.print(urlSistema); %>Definiciones/DefPlanEstudioSWW.jsp?MODO=<%out.print(Enumerado.Modo.UPDATE);%>&pCarCod=<%out.print(CarCod);%>">Regresar</a>
        </div>
    </span>
</header>