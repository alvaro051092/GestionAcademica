<%-- 
    Document   : index
    Created on : 24-jun-2017, 12:34:51
    Author     : alvar
--%>

<%@page import="Enumerado.NombreSesiones"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoIniciar"%>

<%
    String usuario = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
    
    
    session.setAttribute(NombreSesiones.SESSION_COD.getValor(), Utilidades.GetInstancia().GenerarToken(10));
    
    String web = "login.jsp";
    if(usuario == null)
    {
        web = "login.jsp";
        //response.sendRedirect("login.jsp");
        //request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    else
    {
        Boolean esAdm = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
        Boolean esAlu = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
        Boolean esDoc = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
        
        
        
        if(esAdm)
        {
            web = "Definiciones/DefCalendarioGrid.jsp";
            //request.getRequestDispatcher("Definiciones/DefCalendarioGrid.jsp").forward(request, response);
            //response.sendRedirect("Definiciones/DefCalendarioGrid.jsp");
        }else if(esDoc)
        {
            web = "Docente/EstudiosDictados.jsp";
            //response.sendRedirect("Docente/EstudiosDictados.jsp");
            //request.getRequestDispatcher("Docente/EstudiosDictados.jsp").forward(request, response);
        }else if(esAlu)
        { 
            web = "Alumno/Evaluaciones.jsp";
            
            //response.sendRedirect("Alumno/Evaluaciones.jsp");
            //request.getRequestDispatcher("Alumno/Evaluaciones.jsp").forward(request, response);
        }
        
        
        
        //dispatch.forward(request, response);
        
    }
    

        request.getRequestDispatcher(web).forward(request, response);
    
    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica</title>
        <jsp:include page="/masterPage/head.jsp"/>
    </head>
    <body>
        
    </body>
</html>
