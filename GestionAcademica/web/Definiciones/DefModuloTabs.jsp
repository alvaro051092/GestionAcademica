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


<header class="panel-heading tab-bg-dark-navy-blue ">

<%
    String urlSistema = Utilidades.GetInstancia().GetUrlSistema();
    String urlActual    = Utilidades.GetInstancia().GetPaginaActual(request);
    
    Modo Mode           = Modo.valueOf(request.getParameter("MODO"));
    String CurCod       = request.getParameter("pCurCod");
    String ModCod       = request.getParameter("pModCod");
    
    out.println("<ul class='nav nav-tabs'>");
    
    out.println("<li class='" + (urlActual.equals("DefModulo.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefModulo.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefModulo.jsp?MODO=" + Mode + "&pCurCod=" + CurCod + "&pModCod=" + ModCod + "'>Modulo</a></li>");
    if(!Mode.equals(Modo.INSERT)) out.println("<li class='" + (urlActual.equals("DefModuloEvaluacionSWW.jsp") ? "active" : "")+"'><a class='tabs' aria-expanded='"+(urlActual.equals("DefModuloEvaluacionSWW.jsp") ? "true" : "false")+"' href='" + urlSistema + "Definiciones/DefModuloEvaluacionSWW.jsp?MODO=" + Mode + "&pCurCod=" + CurCod + "&pModCod=" + ModCod + "'>Evaluaciones</a></li>");
    
    out.println("</ul>");
%>
    
    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar" href="<% out.print(urlSistema); %>Definiciones/DefCursoModuloSWW.jsp?MODO=UPDATE&pCurCod=<% out.print(CurCod); %>">Regresar</a>
        </div>
    </span>
        
</header>
