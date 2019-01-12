<%-- 
    Document   : DefCursoTabs
    Created on : 03-jul-2017, 18:29:23
    Author     : alvar
--%>

<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoCurso"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();
    String urlActual    = Utilidades.GetInstancia().GetPaginaActual(request);
    Modo Mode           = Modo.valueOf(request.getParameter("MODO"));
    String CurCod       = request.getParameter("pCurCod");
    
    
%>
    
<header class="panel-heading tab-bg-dark-navy-blue ">
    <ul class="nav nav-tabs">
        <% 
            out.println("<li class='" + (urlActual.equals("DefCurso.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefCurso.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefCurso.jsp?MODO=" + Mode + "&pCurCod=" + CurCod + "'>Curso</a></li>");

            if(!Mode.equals(Mode.INSERT)) out.println("<li class='" + (urlActual.equals("DefCursoModuloSWW.jsp") ? "active" : "") + "'><a  class='tabs' aria-expanded='"+(urlActual.equals("DefCursoModuloSWW.jsp") ? "true" : "false")+"'  href='"+ urlSistema + "Definiciones/DefCursoModuloSWW.jsp?MODO=" + Mode + "&pCurCod=" + CurCod + "'>Modulos</a></li>");
            if(!Mode.equals(Mode.INSERT)) out.println("<li class='" + (urlActual.equals("DefCursoEvaluacionSWW.jsp") ? "active" : "") + "'><a  class='tabs' aria-expanded='"+(urlActual.equals("DefCursoEvaluacionSWW.jsp") ? "true" : "false")+"'  href='"+ urlSistema + "Definiciones/DefCursoEvaluacionSWW.jsp?MODO=" + Mode + "&pCurCod=" + CurCod + "'>Evaluaciones</a></li>");

        %>
    </ul>
    
    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar" href="<% out.print(urlSistema); %>Definiciones/DefCursoWW.jsp">Regresar</a>
        </div>
    </span>
</header>