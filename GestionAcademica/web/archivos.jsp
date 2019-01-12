<%-- 
    Document   : archivos
    Created on : 16-sep-2017, 12:31:55
    Author     : alvar
--%>

<%@page import="Entidad.PeriodoEstudioDocumento"%>
<%@page import="Entidad.PeriodoEstudio"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.Persona"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
    String usuario = (String) session.getAttribute(Enumerado.NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Logica.Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    
    List<Object> lstPeriodos = new ArrayList();
    
    if(usuario != null)
    {
        Persona persona = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();
        
       Retorno_MsgObj retorno = LoPeriodo.GetInstancia().EstudioObtenerByPersona(persona.getPerCod());
       
       if(!retorno.SurgioErrorListaRequerida())
       {
           lstPeriodos = retorno.getLstObjetos();
       }
    }

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Documentos</title>
        <jsp:include page="/masterPage/head.jsp"/>

    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <jsp:include page="/masterPage/cabezal_menu.jsp"/>
		<!-- CONTENIDO -->
        <div class="contenido" id="contenedor">                
            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                        <header class="panel-heading">
                            <!-- TITULO -->
                                DOCUMENTOS
                            <!-- BOTONES -->
                        </header>
        
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <% 
                                                            boolean primero = true;
                                                            for(Object objeto : lstPeriodos)
                                                            {
                                                                PeriodoEstudio perEst = (PeriodoEstudio) objeto;
                                                             
                                                                if(perEst.getLstDocumento().size() > 0)
                                                                {
                                                                
                                                                    //------------------------------------------------------------------------
                                                                    //NOMBRE DEL PERIODO
                                                                    //------------------------------------------------------------------------


                                                                    if(primero)
                                                                    {
                                                                        primero = false;
                                                                    }
                                                                    else
                                                                    {
                                                                        out.println("<div class='formulario_borde'></div>");
                                                                    }

                                                                    out.println("<div class='col-lg-offset-3 panel_contenedorTitulo'>");
                                                                        out.println("<h2>" 
                                                                                + perEst.getCarreraCursoNombre() 
                                                                                + " - " 
                                                                                + perEst.getEstudioNombre()
                                                                                + " - "
                                                                                + perEst.getPeriodo().TextoPeriodo()
                                                                                + "</h2>");
                                                                    out.println("</div>");

                                                                    //------------------------------------------------------------------------
                                                                    //DOCUMENTOS
                                                                    //------------------------------------------------------------------------

                                                                    out.println("<div name='div_documentos' class='col-lg-offset-3 col-lg-7'>");

                                                                        for(PeriodoEstudioDocumento doc : perEst.getLstDocumento())
                                                                        {
                                                                            out.println("<div><a target='_blank'  href='" 
                                                                                    + urlSistema + "DescargarArchivo?pArcCod=" + doc.getArcCod() + "'> "
                                                                                    + doc.getDocNom() + "." + doc.getDocExt()
                                                                                    + " </a></div>");
                                                                        }

                                                                    out.println("</div>");
                                                                }
                                                            }
                                                        %>
                                                    </div>
                                                </div>
                                            </section>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>

    </body>
</html>