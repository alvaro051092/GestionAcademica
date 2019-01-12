<%-- 
    Document   : MasterPage
    Created on : 24-jun-2017, 11:43:00
    Author     : alvar
--%>

<%@page import="Logica.LoParametro"%>
<%@page import="Entidad.Menu"%>
<%@page import="Dominio.OpcionesDeMenu"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Persona"%>
<%@page import="Dominio.Sitios"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema       = LoParametro.GetInstancia().obtener().getParUrlSis();
    String sitioActual = utilidad.GetPaginaActual(request);
    
    Boolean esAdm   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
    
    esAdm = (esAdm == null ? false : esAdm);
    esAlu = (esAlu == null ? false : esAlu);
    esDoc = (esDoc == null ? false : esDoc);
%>
    
<div class="sidebar" data-color="red">    
    
        <div id="logo" name="logo" class="logo">
            <a href="<% out.print(urlSistema); %>"> <img src="<% out.print(urlSistema); %>/Imagenes/logo_ctc.png" height="55px" alt="logo del instituto"/></a>
        </div>

    <input type="hidden" name="sga_sitioactual" id="sga_sitioactual" value="<% out.print(sitioActual); %>">    
    
    
    <div class='sidebar-wrapper'>

    <%
        if(esAdm)
        {               

            out.println("<ul class='nav'>");
           /* out.println("   <li class='active'><a href='#'><i class='material-icons'>dashboard</i><p>Inicio</p></a></li>");*/

            for(Menu menu : OpcionesDeMenu.GetInstancia().getLstAdministrador())
            {
                out.println("<li><a href='" + menu.getMenUrl() + "'>" + menu.getMenNom() + "</a></li>");
            }
            
            out.println("</ul>");
        }

        if(esDoc)
        {
            out.println("<label>Docente</label>");
            out.println("<ul class='nav nav-pills nav-stacked'>");
            
            for(Menu menu : OpcionesDeMenu.GetInstancia().getLstDocente())
            {
                out.println("<li><a href='" + menu.getMenUrl() + "'>" + menu.getMenNom() + "</a></li>");
            }
            
            out.println("</ul>");
        }
        
        if(esAlu)
        {
            out.println("<label>Alumno</label>");
            out.println("<ul class='nav nav-pills nav-stacked'>");
            
            for(Menu menu : OpcionesDeMenu.GetInstancia().getLstAlumno())
            {
                out.println("<li><a href='" + menu.getMenUrl() + "'>" + menu.getMenNom() + "</a></li>");
            }
            
            out.println("</ul>");

        }


    %>
    
    </div>
</div>

