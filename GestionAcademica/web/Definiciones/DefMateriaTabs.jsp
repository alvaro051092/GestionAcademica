<%-- 
    Document   : DefMateriaTabs
    Created on : jul 20, 2017, 3:18:00 p.m.
    Author     : aa
--%>

<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();
    
    String urlActual    = Utilidades.GetInstancia().GetPaginaActual(request);
    Modo Mode           = Modo.valueOf(request.getParameter("MODO"));
    String CarCod       = request.getParameter("pCarCod");
    String PlaEstCod    = request.getParameter("pPlaEstCod");
    String MatCod       = request.getParameter("pMatCod");
    
    
%>
    
<header class="panel-heading tab-bg-dark-navy-blue ">
    <ul class="nav nav-tabs">
        <% 
            out.println("<li class='" + (urlActual.equals("DefMateria.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefMateria.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefMateria.jsp?MODO=" + Mode + "&pCarCod=" + CarCod + "&pPlaEstCod=" + PlaEstCod + "&pMatCod=" + MatCod + "'>Materia</a></li>");
            
            if(!Mode.equals(Mode.INSERT)) out.println("<li class='" + (urlActual.equals("DefMateriaPreviaSWW.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefMateriaPreviaSWW.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefMateriaPreviaSWW.jsp?MODO=" + Mode + "&pCarCod=" + CarCod + "&pPlaEstCod=" + PlaEstCod + "&pMatCod=" + MatCod + "'>Previas</a></li>");
            if(!Mode.equals(Mode.INSERT)) out.println("<li class='" + (urlActual.equals("DefMateriaEvaluacionSWW.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefMateriaEvaluacionSWW.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefMateriaEvaluacionSWW.jsp?MODO=" + Mode + "&pCarCod=" + CarCod + "&pPlaEstCod=" + PlaEstCod + "&pMatCod=" + MatCod + "'>Evaluaciones</a></li>");
        %>
    </ul>

    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar" href="<% out.print(urlSistema); %>Definiciones/DefMateriaSWW.jsp?MODO=<%out.print(Enumerado.Modo.UPDATE);%>&pPlaEstCod=<%out.print(PlaEstCod.toString());%>&pCarCod=<%out.print(CarCod.toString());%>">Regresar</a>
        </div>
    </span>
</header>