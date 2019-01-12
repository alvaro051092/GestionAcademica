<%-- 
    Document   : DefCursoWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.Curso"%>
<%@page import="java.util.List"%>
<%@page import="Logica.LoCurso"%>
<%@page import="Utiles.Utilidades"%>
<%

    LoCurso loCurso = LoCurso.GetInstancia();
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
    List<Object> lstCurso = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loCurso.obtenerLista();
    if (retorno.getMensaje().getTipoMensaje() != TipoMensaje.ERROR && retorno.getLstObjetos() != null) {
        System.err.println("Lista de objeto: " + retorno.getLstObjetos().size());
        lstCurso = retorno.getLstObjetos();
        System.err.println("Lista de curso: " + lstCurso.size());
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblCursoVisible = (lstCurso.size() > 0 ? "" : "display: none;");

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Cursos</title>
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
                        <header class="panel-heading">
                            <!-- TITULO -->
                            CURSOS
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Definiciones/DefCurso.jsp?MODO=<% out.print(Enumerado.Modo.INSERT); %>" title="Ingresar" class="glyphicon glyphicon-plus"> </a>
                            </span>
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                    <!-- CONTENIDO -->
                                    <table id='tbl_ww' style=' <% out.print(tblCursoVisible); %>' class='table table-hover'>
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th></th>
                                                <th>Código</th>
                                                <th>Nombre</th>
                                                <th>Descripción</th>
                                                <th>Facultad</th>
                                                <th>Certificación</th>
                                            </tr>
                                        </thead>


                                        <% for (Object objeto : lstCurso) {
                                                Curso curso = (Curso) objeto;
                                        %>
                                        <tr>
                                            <td><a href="<% out.print(urlSistema); %>Definiciones/DefCurso.jsp?MODO=<% out.print(Enumerado.Modo.DELETE); %>&pCurCod=<% out.print(curso.getCurCod()); %>" name="btn_eliminar" id="btn_eliminar" title="Eliminar" class="glyphicon glyphicon-trash"></a></td>
                                            <td><a href="<% out.print(urlSistema); %>Definiciones/DefCurso.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pCurCod=<% out.print(curso.getCurCod()); %>" name="btn_editar" id="btn_editar" title="Editar" class="glyphicon glyphicon-edit"></a></td>
                                            <td><% out.print(utilidad.NuloToVacio(curso.getCurCod())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(curso.getCurNom())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(curso.getCurDsc())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(curso.getCurFac())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(curso.getCurCrt())); %> </td>

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
