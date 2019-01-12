<%-- 
    Document   : DefMateriaWW
    Created on : jul 17, 2017, 4:22:19 p.m.
    Author     : aa
--%>

<%@page import="Entidad.Materia"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Entidad.PlanEstudio"%>
<%@page import="Entidad.Carrera"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Logica.LoCarrera"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
    String PlaEstCod = request.getParameter("pPlaEstCod");
    
    String titulo ="";

    PlanEstudio plan = new PlanEstudio();
    Carrera car = new Carrera();

    List<Materia> lstMaterias = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loCar.obtener(Long.valueOf(CarCod));

    if (retorno.getMensaje().getTipoMensaje() != TipoMensaje.ERROR && retorno.getObjeto() != null) {
        car = (Carrera) retorno.getObjeto();
        plan = car.getPlanEstudioById(Long.valueOf(PlaEstCod));
        lstMaterias = plan.getLstMateria();
        titulo = plan.getCarreraPlanNombre();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblMateriaVisible = (lstMaterias.size() > 0 ? "" : "display: none;");
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Plan estudio <%=titulo%> | Materias</title>
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
                        <jsp:include page="/Definiciones/DefPlanEstudioTabs.jsp"/>
                        <div class="contenedor_agregar">
                            <a href="<% out.print(urlSistema); %>Definiciones/DefMateria.jsp?MODO=<% out.print(Enumerado.Modo.INSERT); %>&pPlaEstCod=<%out.print(PlaEstCod.toString());%>&pCarCod=<%out.print(CarCod.toString());%>" title="Ingresar" class="glyphicon glyphicon-plus"> </a>
                        </div>
                        <div class="panel-body">
                            <div class=" form">
                                <table id='tbl_ww' class='table table-hover' style=' <% out.print(tblMateriaVisible); %>' class='table table-hover'>
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th></th>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Cantidad de Horas</th>
                                            <th>Tipo de Aprobación</th>
                                            <th>Tipo de Período</th>
                                            <th>Valor del Período</th>
                                            <th>Materias Previas</th>
                                        </tr>
                                    </thead>

                                    <%
                                        for (Materia mat : lstMaterias) {
                                    %>
                                    <tr>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefMateria.jsp?MODO=<% out.print(Enumerado.Modo.DELETE); %>&pPlaEstCod=<% out.print(PlaEstCod.toString()); %>&pCarCod=<% out.print(CarCod.toString()); %>&pMatCod=<% out.print(mat.getMatCod().toString()); %>" name="btn_eliminar" id="btn_eliminar" title="Eliminar" class="glyphicon glyphicon-trash"></a></td>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefMateria.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pPlaEstCod=<% out.print(PlaEstCod.toString()); %>&pCarCod=<% out.print(CarCod.toString()); %>&pMatCod=<% out.print(mat.getMatCod().toString()); %>" name="btn_editar" id="btn_editar" title="Editar" class="glyphicon glyphicon-edit"></a></td>
                                        <td><% out.print(utilidad.NuloToVacio(mat.getMatCod())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(mat.getMatNom())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(mat.getMatCntHor())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(mat.getMatTpoApr().getTipoAprobacionN())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(mat.getMatTpoPer().getTipoPeriodoNombre())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(mat.getMatPerVal())); %> </td>
                                        <td><%out.print(utilidad.NuloToCero(mat.getLstPrevias().size())); %></td>
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
