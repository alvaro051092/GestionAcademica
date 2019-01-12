<%-- 
    Document   : Estudios
    Created on : 26-jul-2017, 16:37:59
    Author     : alvar
--%>

<%@page import="Entidad.Modulo"%>
<%@page import="Entidad.Curso"%>
<%@page import="Enumerado.Colores"%>
<%@page import="Logica.LoCalendario"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="Entidad.Materia"%>
<%@page import="Entidad.PlanEstudio"%>
<%@page import="Entidad.Escolaridad"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Persona"%>
<%@page import="SDT.SDT_PersonaEstudio"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();

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
    ArrayList<SDT_PersonaEstudio> lstEstudio = new ArrayList<>();
    Persona persona = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();
    lstEstudio = LoPersona.GetInstancia().ObtenerEstudios(persona.getPerCod());

    String tblVisible = (lstEstudio.size() > 0 ? "" : "display: none;");


%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Estudios</title>
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
                            ESTUDIOS
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <div name="cont_estudio" class="col-lg-12" style='margin-top:15px; <% out.print(tblVisible); %>'>

                                    <svg name="lines" id="lines" width="100%" height="100%" style="position: absolute; left: 0; top:0">

                                    </svg>

                                    <%
                                        
                                        
                                        for (SDT_PersonaEstudio est : lstEstudio) {
                                            //--------------------------------------------------------------------------------------------------------
                                            //INICIAMOS DIV INSCRIPCION
                                            //--------------------------------------------------------------------------------------------------------
                                            out.println("<div name='div_inscripcion' class='row'>");

                                            if (est.getInscripcion().getPlanEstudio() != null) {
                                                Colores color = Colores.PRIMERO;
                                                //--------------------------------------------------------------------------------------------------------
                                                //INSCRIPCION NOMBRE
                                                //--------------------------------------------------------------------------------------------------------

                                                PlanEstudio plan = est.getInscripcion().getPlanEstudio();
                                                //MOSTRAR TODAS LAS MATERIAS DEL PLAN.

                                                out.println("<div name='div_inscripcion_nombre' class='col-lg-12'><h2>" + plan.getCarreraPlanNombre() + "</h2></div>");

                                                Double periodo = 0.0;
                                                boolean cerrarDivPeriodo = false;

                                                for (Materia materia : plan.getLstMateria()) {
                                                    //--------------------------------------------------------------------------------------------------------
                                                    //MANEJAMOS DIV CONTENEDOR SEMESTRE
                                                    //--------------------------------------------------------------------------------------------------------
                                                    if (!materia.getMatPerVal().equals(periodo)) {
                                                        periodo = materia.getMatPerVal();

                                                        if (cerrarDivPeriodo) {
                                                            //--------------------------------------------------------------------------------------------------------
                                                            //FINALIZAMOS DIV CONTENEDOR SEMESTRE
                                                            //--------------------------------------------------------------------------------------------------------

                                                            out.println("</div>");
                                                            out.println("</div>");
                                                        }

                                                        //--------------------------------------------------------------------------------------------------------
                                                        //INICIAMOS DIV CONTENEDOR SEMESTRE
                                                        //--------------------------------------------------------------------------------------------------------
                                                        out.println("<div class='estudios_contenedorPrincipal'>");
                                                            out.println("<div class='estudios_textoSemestre hidden-xs' style='background-color: "+color.getValor()+";'>" + materia.getMatTpoPer().getTipoPeriodoNombre() + ": " + materia.getMatPerVal() + "</div>");
                                                            out.println("<div name='div_semestre' class='estudios_contenedorSemestre col-lg-12'> ");
                                                                out.println("<div class='estudios_semestreMobile' style='background-color: "+color.getValor()+";'>" + materia.getMatTpoPer().getTipoPeriodoNombre() + ": " + materia.getMatPerVal() + "</div>");
                                                                cerrarDivPeriodo = true;
                                                                color = (color.ordinal() +1 == Colores.values().length ? Colores.PRIMERO : Colores.values()[color.ordinal() + 1]);
                                                    }

                                                    //--------------------------------------------------------------------------------------------------------
                                                    //INICIO DIV MATERIA
                                                    //--------------------------------------------------------------------------------------------------------
                                                    out.println("<div class='estudios_contenedorMaterias col-lg-3' id='dv_mat_" + materia.getMatCod() + "' data-materia='" + materia.getMatNom() + "' data-id='" + materia.getMatCod() + "' data-previas='" + materia.ObtenerPreviasCodigos() + "'><div class='caja_estudio'>");

                                                    String progreso = "";
                                                    String escolaridad = "<div class='estudios_contenedorEscolaridad' name='escolaridad'>";

                                                    if (LoCalendario.GetInstancia().AlumnoCursoEstudio(persona.getPerCod(), materia, null, null)) {
                                                        //--------------------------------------------------------------------------------------------------------
                                                        //ALUMNO CURSO MATERIA
                                                        //--------------------------------------------------------------------------------------------------------
                                                        progreso = "En curso";

                                                        for (Escolaridad esc : est.getEscolaridad()) {
                                                            if (esc.getMateria() != null) {
                                                                if (esc.getMateria().getMatCod().equals(materia.getMatCod())) {
                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    //SE APLICA ESTADO DE MATERIA
                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    if (esc.Revalida()) {
                                                                        progreso = "Revalida";
                                                                    } else if (esc.getAprobado()) {
                                                                        progreso = "Cursada";
                                                                    }

                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    //SE AGREGA DATO DE ESCOLARIDAD
                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    escolaridad += "<div name='una_escolaridad'>";
                                                                    escolaridad += "<div>Fecha: <label>" + esc.getEscFch() + "</label></div>\n";
                                                                    escolaridad += "<div>Curso: <label>" + (esc.getEscCurVal() == null ? "" : esc.getEscCurVal()) + "</label></div>\n";
                                                                    escolaridad += "<div>Examen: <label>" + (esc.getEscCalVal() == null ? "" : esc.getEscCalVal()) + "</label></div>\n";
                                                                    escolaridad += "<div>Estado: <label>" + esc.getAprobacion() + "</label></div>\n";
                                                                    escolaridad += "</div>";
                                                                }
                                                            }

                                                        }

                                                    } else {
                                                        //--------------------------------------------------------------------------------------------------------
                                                        //ALUMNO NO CURSO MATERIA
                                                        //--------------------------------------------------------------------------------------------------------
                                                        progreso = "No cursada";
                                                    }

                                                    escolaridad += "</div>";

                                                    //-NOMBRE
                                                    out.println("<div class='estudios_tituloMateria'>" + materia.getMatNom() + "</div>");

                                                    //-PROGRESO
                                                    out.println("<div>Progreso: <label>" + progreso + "</label></div>");

                                                    out.println(escolaridad);

                                                    //--------------------------------------------------------------------------------------------------------
                                                    //FIN DIV MATERIA
                                                    //--------------------------------------------------------------------------------------------------------
                                                    out.println("</div></div>");
                                                }

                                                //--------------------------------------------------------------------------------------------------------
                                                //FINALIZAMOS DIV CONTENEDOR SEMESTRE SI QUEDO ABIERTO
                                                //--------------------------------------------------------------------------------------------------------
                                                if (plan.getLstMateria() != null) {
                                                    if (plan.getLstMateria().size() > 0) {
                                                        out.println("</div>");
                                                        out.println("</div>");
                                                    }
                                                }

                                            }

                                            if (est.getInscripcion().getCurso() != null) {
                                                Colores color = Colores.PRIMERO;
                                                //MOSTRAR TODAS LOS MODULOS DEL PLAN.
                                                
                                                //--------------------------------------------------------------------------------------------------------
                                                //INSCRIPCION NOMBRE
                                                //--------------------------------------------------------------------------------------------------------

                                                Curso curso = est.getInscripcion().getCurso();
                                                //MOSTRAR TODAS LAS MATERIAS DEL PLAN.

                                                out.println("<div name='div_inscripcion_nombre' class='col-lg-12'><h2>" + curso.getCurNom() + "</h2></div>");

                                                Double periodo = 0.0;
                                                boolean cerrarDivPeriodo = false;

                                                for (Modulo modulo : curso.getLstModulos()) {
                                                    //--------------------------------------------------------------------------------------------------------
                                                    //MANEJAMOS DIV CONTENEDOR SEMESTRE
                                                    //--------------------------------------------------------------------------------------------------------
                                                    if (!modulo.getModPerVal().equals(periodo)) {
                                                        periodo = modulo.getModPerVal();

                                                        if (cerrarDivPeriodo) {
                                                            //--------------------------------------------------------------------------------------------------------
                                                            //FINALIZAMOS DIV CONTENEDOR SEMESTRE
                                                            //--------------------------------------------------------------------------------------------------------

                                                            out.println("</div>");
                                                            out.println("</div>");
                                                        }

                                                        //--------------------------------------------------------------------------------------------------------
                                                        //INICIAMOS DIV CONTENEDOR SEMESTRE
                                                        //--------------------------------------------------------------------------------------------------------
                                                        out.println("<div class='estudios_contenedorPrincipal'>");
                                                        out.println("<div class='estudios_textoSemestre hidden-xs' style='background-color: "+color.getValor()+";'>" + modulo.getModTpoPer().getTipoPeriodoNombre() + ": " + modulo.getModPerVal() + "</div>");
                                                        out.println("<div name='div_semestre' class='estudios_contenedorSemestre col-lg-12'> ");
                                                        out.println("<div class='estudios_semestreMobile' style='background-color: "+color.getValor()+";'>" + modulo.getModTpoPer().getTipoPeriodoNombre() + ": " + modulo.getModPerVal() + "</div>");
                                                        cerrarDivPeriodo = true;
                                                        color = (color.ordinal() +1 == Colores.values().length ? Colores.PRIMERO : Colores.values()[color.ordinal() + 1]);
                                                    }

                                                    //--------------------------------------------------------------------------------------------------------
                                                    //INICIO DIV MATERIA
                                                    //--------------------------------------------------------------------------------------------------------
                                                    out.println("<div class='estudios_contenedorMaterias col-lg-3' id='dv_mat_" + modulo.getModCod() + "' data-materia='" + modulo.getModNom() + "' data-id='" + modulo.getModCod() + "' ><div class='caja_estudio'>");

                                                    String progreso = "";
                                                    String escolaridad = "<div class='estudios_contenedorEscolaridad' name='escolaridad'>";

                                                    if (LoCalendario.GetInstancia().AlumnoCursoEstudio(persona.getPerCod(), null, modulo, null)) {
                                                        //--------------------------------------------------------------------------------------------------------
                                                        //ALUMNO CURSO MATERIA
                                                        //--------------------------------------------------------------------------------------------------------
                                                        progreso = "En curso";

                                                        for (Escolaridad esc : est.getEscolaridad()) {
                                                            if (esc.getModulo() != null) {
                                                                if (esc.getModulo().getModCod().equals(modulo.getModCod())) {
                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    //SE APLICA ESTADO DE MATERIA
                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    if (esc.Revalida()) {
                                                                        progreso = "Revalida";
                                                                    } else if (esc.getAprobado()) {
                                                                        progreso = "Cursada";
                                                                    }

                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    //SE AGREGA DATO DE ESCOLARIDAD
                                                                    //--------------------------------------------------------------------------------------------------------
                                                                    escolaridad += "<div name='una_escolaridad'>";
                                                                    escolaridad += "<div>Fecha: <label>" + esc.getEscFch() + "</label></div>\n";
                                                                    escolaridad += "<div>Curso: <label>" + (esc.getEscCurVal() == null ? "" : esc.getEscCurVal()) + "</label></div>\n";
                                                                    escolaridad += "<div>Examen: <label>" + (esc.getEscCalVal() == null ? "" : esc.getEscCalVal()) + "</label></div>\n";
                                                                    escolaridad += "<div>Estado: <label>" + esc.getAprobacion() + "</label></div>\n";
                                                                    escolaridad += "</div>";
                                                                }
                                                            }

                                                        }

                                                    } else {
                                                        //--------------------------------------------------------------------------------------------------------
                                                        //ALUMNO NO CURSO MATERIA
                                                        //--------------------------------------------------------------------------------------------------------
                                                        progreso = "No cursada";
                                                    }

                                                    escolaridad += "</div>";

                                                    //-NOMBRE
                                                    out.println("<div class='estudios_tituloMateria'>" + modulo.getModNom() + "</div>");

                                                    //-PROGRESO
                                                    out.println("<div>Progreso: <label>" + progreso + "</label></div>");

                                                    out.println(escolaridad);

                                                    //--------------------------------------------------------------------------------------------------------
                                                    //FIN DIV MATERIA
                                                    //--------------------------------------------------------------------------------------------------------
                                                    out.println("</div></div>");
                                                }

                                                //--------------------------------------------------------------------------------------------------------
                                                //FINALIZAMOS DIV CONTENEDOR SEMESTRE SI QUEDO ABIERTO
                                                //--------------------------------------------------------------------------------------------------------
                                                if (curso.getLstModulos() != null) {
                                                    if (curso.getLstModulos().size() > 0) {
                                                        out.println("</div>");
                                                        out.println("</div>");
                                                    }
                                                }
                                            }

                                            //--------------------------------------------------------------------------------------------------------
                                            //FIN DIV INSCRIPCION
                                            //--------------------------------------------------------------------------------------------------------
                                            out.println("</div>");

                                        }
                                    %>
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