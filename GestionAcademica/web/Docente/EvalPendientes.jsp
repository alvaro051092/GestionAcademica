<%-- 
    Document   : EvalPendientes
    Created on : jul 27, 2017, 3:06:54 p.m.
    Author     : aa
--%>

<%@page import="Entidad.PeriodoEstudio"%>
<%@page import="Entidad.Periodo"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Logica.LoCalendario"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Calendario"%>
<%@page import="Entidad.Persona"%>
<%--<%@page import="Persistencia.PerCalendario"%>--%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    LoPersona lopersona = LoPersona.GetInstancia();
    LoCalendario loCalendario = LoCalendario.GetInstancia();
    LoPeriodo loPeriodo = LoPeriodo.GetInstancia();
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
    Long pPeriEstCod = null;
//    System.err.println("PERIESTCOD: " + request.getParameter("pPeriEstCod"));
    if(request.getParameter("pPeriEstCod") != null) pPeriEstCod = Long.valueOf(request.getParameter("pPeriEstCod"));
        
    Retorno_MsgObj retPersona = lopersona.obtenerByMdlUsr(usuario);
    persona = (Persona) retPersona.getObjeto();

    List<Object> lstObjeto = new ArrayList<>();
    
    
    Retorno_MsgObj retorno = loCalendario.ObtenerEvaluacionesDocentes(persona.getPerCod());
    
    if (!retorno.SurgioError() && !retPersona.SurgioErrorObjetoRequerido()) 
    {
        Long mat = 0L;
        Long mod = 0L;
        
        if(pPeriEstCod != null)
        {
            Retorno_MsgObj retPeri =  loPeriodo.EstudioObtener(pPeriEstCod);
            if(!retPeri.SurgioErrorObjetoRequerido())
            {
                PeriodoEstudio p = (PeriodoEstudio) retPeri.getObjeto();

                if(p.getMateria() != null)
                {
                    mat = p.getMateria().getMatCod();
                }
                if(p.getModulo() != null)
                {
                    mod = p.getModulo().getModCod();
                }

                for(Object objc : retorno.getLstObjetos())
                {
                    Calendario c = (Calendario) objc;

                    if(c.getEvaluacion().getMatEvl() != null && c.getEvaluacion().getMatEvl().getMatCod() == mat)
                    {
                        lstObjeto.add(c);
                    }
                    if(c.getEvaluacion().getModEvl() != null && c.getEvaluacion().getModEvl().getModCod() == mod)
                    {
                        lstObjeto.add(c);
                    }
                }
            }
            else
            {
                out.print(retPeri.getMensaje().toString());
            }
        }
        else
        {
            lstObjeto = retorno.getLstObjetos();
        }
    } 
    else 
    {
        if(retorno.getMensaje() != null)
        {
            out.print(retorno.getMensaje().toString());
        }
        else
        {
            out.print(retPersona.getMensaje().toString());
        }
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Evaluaciones | Docente</title>
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
                            EVALUACIONES
                        </header>

                        <div class="panel-body">
                            <div class=" form">
                                <table class='table table-hover tabla_responsive' style=' <% out.print(tblVisible); %>'>
                                    <thead>
                                        <tr>
                                            <th>Código</th>
                                            <th>Evaluación</th>
                                            <th>Carrera/Curso</th>
                                            <th>Estudio</th>
                                            <th>Fecha</th>
                                            <th>Fecha Desde</th>
                                            <th>Fecha Hasta</th>
                                            <th></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <%
                                            for (Object objeto : lstObjeto) {
                                                Calendario calendario = (Calendario) objeto;
                                        %>
                                        <tr>
                                            <td><%out.print(utilidad.NuloToVacio(calendario.getCalCod())); %> </td>
                                            <td><%out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEvlNom())); %> </td>
                                            <td><%out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getCarreraCursoNombre()));%></td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEstudioNombre())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getCalFch()));%> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchDsd()));%></td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchHst()));%></td>
                                            <td><a href="<% out.print(urlSistema); %>Docente/CalificarAlumnos.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pCalCod=<% out.print(calendario.getCalCod()); %>" name="btn_edit_doc" id="btn_edit_doc" title="Evaluar" class="glyphicon glyphicon-paste"/></td>
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