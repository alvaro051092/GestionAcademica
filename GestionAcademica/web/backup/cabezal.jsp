<%-- 
    Document   : cabezal
    Created on : 24-jun-2017, 11:48:48
    Author     : alvar
--%>

<%@page import="Logica.LoParametro"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.Accion"%>
<%@page import="Dominio.Sitios"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Persona"%>
<%@page import="java.net.URL"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    String usuario          = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
    String urlSistema       = LoParametro.GetInstancia().obtener().getParUrlSis();
    Boolean logueado        = false;
    
    if(usuario != null)
    {
        if(!usuario.isEmpty())
        {
            logueado = true;
        }
    }
    
    
%>

        <input type="hidden" name="sga_url" id="sga_url" value="<% out.print(urlSistema); %>">    
        
    
        
        <% if(logueado)
        {
            %>
                <jsp:include page='/log_out.jsp'/>
            <%
        }
        else
        {
            %>
                <jsp:include page='/login.jsp' />
            <%
        }
        %>

        
      




