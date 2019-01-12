<%-- 
    Document   : EvalPendientes
    Created on : jul 23, 2017, 3:43:29 p.m.
    Author     : aa
--%>

<%@page import="Entidad.Evaluacion"%>
<%@page import="Entidad.PeriodoEstudio"%>
<%@page import="Entidad.PeriodoEstudioDocente"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Entidad.Periodo"%>
<%@page import="SDT.SDT_PersonaEstudio"%>
<%@page import="Entidad.Persona"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    LoPersona lopersona = LoPersona.GetInstancia();
    LoPeriodo loPer = LoPeriodo.GetInstancia();
    Utilidades utilidad = Utilidades.GetInstancia();
    
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
    Persona persona = new Persona();
    Periodo per = new Periodo();

    List<Object> lstPer = new ArrayList<>();
    List<PeriodoEstudio> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retPersona = lopersona.obtenerByMdlUsr(usuario);
    persona = (Persona) retPersona.getObjeto();

    Retorno_MsgObj retorno = loPer.obtenerLista();

    if (!retorno.SurgioError() && !retPersona.SurgioErrorObjetoRequerido()) {
        lstPer = retorno.getLstObjetos();
        for (Object obj : lstPer) {
            per = (Periodo) obj;
            for (PeriodoEstudio periodoEstudio : per.getLstEstudio()) {
                if (periodoEstudio.getExisteDocente(persona.getPerCod())) {
                    lstObjeto.add(periodoEstudio);
                }
            }
        }
    } else if (retorno.SurgioError()) {
        out.print(retorno.getMensaje().toString());
    } else {
        out.print(retPersona.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");


%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Estudios | Docente</title>
        <jsp:include page="/masterPage/head.jsp"/>
        
        
        <script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/js/dataTables.responsive.min.js"></script>
        <link href="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet" type="text/css"/>
        
        <script>
            $('.tabla_responsive').hide();
            MostrarCargando(true);

            $(document).ready(function() {



                    //---------------------------------------------------------
                    //TABLA CON REPORTES
                    //---------------------------------------------------------
                    $('.tabla_responsive').DataTable({
                        "responsive": true,
                        "processing": true,
                        deferRender: true,
                        "lengthMenu": [ [10, 20, 50, -1], [10, 20, 50, "Todos"] ],
                        pageLength: 20,
                        "fnInitComplete": function(oSettings, json) {
                                MostrarCargando(false);
                                $('.tabla_responsive').show();
                              },
                        "ordering": false,
                        dom: '',
                        "language": {
                                "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                            }
                    });

            } );
        </script>
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
                            ESTUDIOS
                            
                        </header>
                
                        <div class="panel-body">
                            <div class=" form">
                                <table class='table table-hover tabla_responsive' style=' <% out.print(tblVisible); %>'>
                                    <thead>
                                        <tr>
                                            <th>Código</th>
                                            <th>Nombre</th>
                                            <th>Tipo de Estudio</th>
                                            <th>Alumnos</th>
                                            <th>Docentes</th>
                                            <th></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% 
                                            for (PeriodoEstudio perEstudio : lstObjeto) {
                                        %>
                                        <tr>
                                            <td><%out.print(utilidad.NuloToVacio(perEstudio.getPeriEstCod())); %> </td>
                                            <td><%out.print(utilidad.NuloToVacio(perEstudio.getEstudioNombre())); %> </td>
                                            <td><%out.print(utilidad.NuloToVacio(perEstudio.getEstudioTipo()));%></td>
                                            <td><% out.print(utilidad.NuloToVacio(perEstudio.getCantidadAlumnos())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio((perEstudio.getCantidadDocente())));%> </td>
                                            <td><a href="<% out.print(urlSistema); %>Docente/EvalPendientes.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pPeriEstCod=<% out.print(perEstudio.getPeriEstCod()); %>" name="btn_edit_doc" id="btn_edit_doc" title="Evaluaciones" class="glyphicon glyphicon-paste"/></td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
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