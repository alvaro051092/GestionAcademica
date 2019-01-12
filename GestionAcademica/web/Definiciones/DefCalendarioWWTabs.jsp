<%-- 
    Document   : DefPersonaTabs
    Created on : 30-jun-2017, 20:44:43
    Author     : alvar
--%>

<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Entidad.Persona"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>


<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

    String urlActual = utilidad.GetPaginaActual(request);
%>

<header class="panel-heading tab-bg-dark-navy-blue ">
    <ul class="nav nav-tabs">
        <li class="<% out.print((urlActual.equals("DefCalendarioGrid.jsp") ? "active" : "")); %>"><a class='tabs' aria-expanded='<%out.print((urlActual.equals("DefCalendarioGrid.jsp") ? "true" : "false"));%>' href='<% out.print(urlSistema); %>Definiciones/DefCalendarioGrid.jsp'>Calendario</a></li>
        <li class="<% out.print((urlActual.equals("DefCalendarioWW.jsp") ? "active" : "")); %>"><a class='tabs' aria-expanded='<%out.print((urlActual.equals("DefCalendarioWW.jsp") ? "true" : "false"));%>' href='<% out.print(urlSistema); %>Definiciones/DefCalendarioWW.jsp'>Listado</a></li>
    </ul>
</header>