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

    LoPersona loPersona = LoPersona.GetInstancia();
    String urlSistema = Utilidades.GetInstancia().GetUrlSistema();
    String urlActual    = Utilidades.GetInstancia().GetPaginaActual(request);
   
    Modo Mode           = Modo.valueOf(request.getParameter("MODO"));
    String PerCod       = request.getParameter("pPerCod");
    
    boolean estudiosVisible     = false;
    boolean escolaridadVisible  = false;
    
    if(!Mode.equals(Mode.INSERT))
    {   
        Persona persona     = new Persona();

        persona = (Persona) loPersona.obtener(Long.valueOf(PerCod)).getObjeto();
        
        estudiosVisible     = (persona.getPerEsAlu());
        escolaridadVisible  = (persona.getPerEsAlu());
            
    }
    
    
%>

<header class="panel-heading tab-bg-dark-navy-blue ">
    <ul class="nav nav-tabs">
        <% 
            out.println("<li class='" + (urlActual.equals("DefPersona.jsp") ? "active" : "") + "'><a  class='tabs' aria-expanded='"+(urlActual.equals("DefPersona.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefPersona.jsp?MODO=" + Mode + "&pPerCod=" + PerCod + "'>Persona</a></li>");

            if(estudiosVisible) out.println("<li class='" + (urlActual.equals("DefPersonaInscripcionSWW.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPersonaInscripcionSWW.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefPersonaInscripcionSWW.jsp?MODO=" + Mode + "&pPerCod=" + PerCod + "'>Inscripciones</a></li>");
            if(escolaridadVisible) out.println("<li class='" + (urlActual.equals("DefPersonaEscolaridadSWW.jsp") ? "active" : "") + "'><a class='tabs' aria-expanded='"+(urlActual.equals("DefPersonaEscolaridadSWW.jsp") ? "true" : "false")+"' href='"+ urlSistema + "Definiciones/DefPersonaEscolaridadSWW.jsp?MODO=" + Mode + "&pPerCod=" + PerCod + "'>Escolaridad</a></li>");

        %>
    </ul>
    
    <span class="tools pull-right">
        <div class="hidden-xs">
            <a class="tabs_regresar" href="<% out.print(urlSistema); %>Definiciones/DefPersonaWW.jsp">Regresar</a>
        </div>
    </span>
</header>
