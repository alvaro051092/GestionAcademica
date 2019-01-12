<%-- 
    Document   : DefPlanEstudioWW
    Created on : jul 7, 2017, 8:10:09 p.m.
    Author     : aa
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.Carrera"%>
<%@page import="Logica.LoCarrera"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.Modo"%>
<%@page import="java.util.List"%>
<%@page import="Entidad.PlanEstudio"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Utilidades utilidad = Utilidades.GetInstancia();
    LoCarrera loCar = LoCarrera.GetInstancia();
    
    String urlSistema = utilidad.GetUrlSistema();
    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------

    String usuario = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    String CarCod = request.getParameter("pCarCod");
    String titulo = "";
    List<PlanEstudio> lstPlanEstudio = new ArrayList<>();
    Carrera car = new Carrera();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loCar.obtener(Long.valueOf(CarCod));
    if (!retorno.SurgioErrorObjetoRequerido()) {
        car = (Carrera) retorno.getObjeto();
        lstPlanEstudio = car.getPlan();
        titulo = car.getCarNom();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblPlanEstudioVisible = (lstPlanEstudio.size() > 0 ? "" : "display: none;");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Carrera <%=titulo%> | Planes de Estudio</title>
        <jsp:include page="/masterPage/head.jsp"/>
        <jsp:include page="/masterPage/head_tables.jsp"/>
    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <jsp:include page="/masterPage/cabezal_menu.jsp"/>
        
        <!-- CONTENIDO -->
        <div class="contenido" id="contenedor">

            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                        <jsp:include page="/Definiciones/DefCarreraTabs.jsp"/>
                        <div class="contenedor_agregar">
                            <a href="<% out.print(urlSistema); %>Definiciones/DefPlanEstudio.jsp?MODO=<% out.print(Enumerado.Modo.INSERT); %>&pCarCod=<%out.print(CarCod.toString());%>" title="Ingresar" class="glyphicon glyphicon-plus"> </a>
                        </div>                     
                        <div class="panel-body">
                            <div class=" form">
                                <table id='tbl_ww' class='table table-hover' style=' <% out.print(tblPlanEstudioVisible); %>'>
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th></th>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Descripción</th>
                                            <th>Creditos Necesarios</th>
                                        </tr>
                                    </thead>
                                    <%
                                        for (PlanEstudio PE : lstPlanEstudio) {
                                    %>
                                    <tr>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefPlanEstudio.jsp?MODO=<% out.print(Enumerado.Modo.DELETE); %>&pCarCod=<% out.print(CarCod.toString()); %>&pPlaEstCod=<% out.print(PE.getPlaEstCod()); %>" name="btn_eliminar" id="btn_eliminar" class="glyphicon glyphicon-trash"></a></td>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefPlanEstudio.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pCarCod=<% out.print(CarCod.toString()); %>&pPlaEstCod=<% out.print(PE.getPlaEstCod()); %>" name="btn_editar" id="btn_editar" class="glyphicon glyphicon-edit"></a></td>
                                        <td><% out.print(utilidad.NuloToCero(PE.getPlaEstCod())); %></td>
                                        <td><% out.print(utilidad.NuloToVacio(PE.getPlaEstNom())); %></td>
                                        <td><% out.print(utilidad.NuloToVacio(PE.getPlaEstDsc())); %></td>
                                        <td><% out.print(utilidad.NuloToVacio(PE.getPlaEstCreNec())); %></td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                </table>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
        <jsp:include page="/masterPage/footer.jsp"/>
    </body>
</html>
