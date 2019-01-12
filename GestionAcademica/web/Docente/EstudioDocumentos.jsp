<%-- 
    Document   : DocEstudioDocumentos
    Created on : jul 27, 2017, 11:11:42 a.m.
    Author     : aa
--%>

<%@page import="java.util.Date"%>
<%@page import="Utiles.Mensajes"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Entidad.PeriodoEstudio"%>
<%@page import="Entidad.PeriodoEstudioDocumento"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Enumerado.Modo"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.List"%>
<%@page import="Logica.Seguridad"%>
<%@page import="java.io.File"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoPeriodo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%

    LoPeriodo loPeriodo = LoPeriodo.GetInstancia();
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = (String) session.getAttribute(NombreSesiones.URL_SISTEMA.getValor());

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
    File fichero = null;
    String PeriEstCod = request.getParameter("pPeriEstCod");
    String ModoTxt = request.getParameter("MODO");

    if (ModoTxt == null) {

        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();

                    //--------------------------------
                    //CARGAR VARIABLES
                    //--------------------------------
                    switch (fieldName) {
                        case "pPeriEstCod":
                            PeriEstCod = fieldValue;
                            break;
                        case "MODO":
                            ModoTxt = fieldValue;
                            break;
                    }
                    //--------------------------------

                } else {
                    fichero = new File("C:/librerias", item.getName());
                    item.write(fichero);
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
    }

    String urlRetorno = urlSistema + "Docente/EstudiosDictados.jsp";

    Modo Mode = Modo.valueOf(ModoTxt);

    if (Mode.equals(Modo.INSERT)) {
        Retorno_MsgObj retorno = AgregarDatos(fichero, Long.valueOf(PeriEstCod));
        if (!retorno.SurgioError()) {
            response.sendRedirect(urlSistema + "Docente/EstudioDocumentos.jsp?MODO=" + Modo.UPDATE + "&pPeriEstCod=" + PeriEstCod);
        } else {
            out.println(retorno.getMensaje());
        }
    }

    List<PeriodoEstudioDocumento> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.EstudioObtener(Long.valueOf(PeriEstCod));
    if (!retorno.SurgioErrorObjetoRequerido()) {
        lstObjeto = ((PeriodoEstudio) retorno.getObjeto()).getLstDocumento();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");


%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Estudios | Documentos</title>
        <jsp:include page="/masterPage/head.jsp"/>


    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <div class="wrapper">
            <jsp:include page="/masterPage/menu_izquierdo.jsp" />

            <div id="contenido" name="contenido" class="main-panel">

                <div class="contenedor-cabezal">
                    <jsp:include page="/masterPage/cabezal.jsp"/>
                </div>

                <div class="contenedor-principal">
                    <div class="col-sm-11 contenedor-texto-titulo-flotante">

                        <div class="contenedor-titulo">    
                            <p>Documentos</p>
                        </div>

                        <div class=""> 
                            <div class="" style="text-align: right;"><a href="<% out.print(urlRetorno); %>">Regresar</a></div>
                        </div>

                        <div style="text-align: right; padding-top: 6px; padding-bottom: 6px;">
                            <a href="#" title="Ingresar" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#PopUpAgregar"> </a>
                        </div>

                        <table style=' <% out.print(tblVisible); %>' class='table table-hover'>
                            <thead><tr>
                                    <th></th>
                                    <th>Código</th>
                                    <th>Nombre</th>
                                    <th>Extension</th>
                                    <th>Fecha</th>
                                    <th></th>
                                </tr>
                            </thead>

                            <tbody>
                                <% for (PeriodoEstudioDocumento periDocumento : lstObjeto) {

                                %>
                                <tr>
                                    <td><% out.print("<a href='#' data-codigo='" + periDocumento.getDocCod() + "' data-nombre='" + periDocumento.getDocNom() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                    <td><% out.print(utilidad.NuloToVacio(periDocumento.getDocCod())); %> </td>
                                    <td><% out.print(utilidad.NuloToVacio(periDocumento.getDocNom())); %> </td>
                                    <td><% out.print(utilidad.NuloToVacio(periDocumento.getDocExt())); %> </td>
                                    <td><% out.print(utilidad.NuloToVacio(periDocumento.getDocFch())); %> </td>
                                    <td><% out.print("<a  target='_blank' href='" + urlSistema + "DescargarArchivo?pPeriEstCod=" + PeriEstCod + "&pDocCod=" + periDocumento.getDocCod() + "' name='btn_descargar' id='btn_descargar' title='Descargar' class='glyphicon glyphicon-save btn_descargar'/>"); %> </td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <jsp:include page="/masterPage/footer.jsp"/>
        </div>

        <!-- PopUp para Agregar personas del calendario -->

        <div id="PopUpAgregar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Documento</h4>
                    </div>
                    <form action="EstudioDocumentos.jsp" enctype="MULTIPART/FORM-DATA" method="post">
                        <div class="modal-body">

                            <div>
                                <input type="hidden" name="PeriEstCod" id="PeriEstCod" value="<% out.print(PeriEstCod); %>">
                                <input type="hidden" name="MODO" id="MODO" value="INSERT">
                                <input type="hidden" name="pPeriEstCod" id="pPeriEstCod" value="<% out.print(PeriEstCod); %>">
                                <input type="file" name="file" required/><br/>

                            </div>
                        </div>

                        <div class="modal-footer">
                            <input type="submit" value="Subir archivo" class="btn btn-success"/>
                            <input type="button" value="Cancelar" class="btn btn-default" data-dismiss="modal" />
                        </div>
                    </form>
                </div>
            </div>

        </div>

        <!------------------------------------------------->

        <!-- PopUp para Eliminar -->

        <div id="PopUpEliminar"  class="modal fade" role="dialog">

            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Eliminar</h4>
                    </div>
                    <div class="modal-body">

                        <p>Eliminar la inscripción de: <label name="elim_nombre" id="elim_nombre"></label></p>
                        <p>Quiere proceder?</p>

                    </div>
                    <div class="modal-footer">
                        <button name="elim_boton_confirmar" id="elim_boton_confirmar" type="button" class="btn btn-danger" data-codigo="">Eliminar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                $(document).ready(function ()
                {

                    $('.btn_eliminar').on('click', function (e)
                    {
                        var codigo = $(this).data("codigo");
                        var nombre = $(this).data("nombre");

                        $('#elim_nombre').text(nombre);
                        $('#elim_boton_confirmar').data('codigo', codigo);
                    });

                    $('#elim_boton_confirmar').on('click', function (e)
                    {
                        var PeriEstCod = $('#PeriEstCod').val();
                        var codigo = $('#elim_boton_confirmar').data('codigo');

                        $.post('<% out.print(urlSistema); %>AB_PeriodoEstudioDocumento',
                                {
                                    pPeriEstCod: PeriEstCod,
                                    pDocCod: codigo,
                                    pAction: "<% out.print(Modo.DELETE);%>"
                                }
                        , function (responseText)
                        {
                            var obj = JSON.parse(responseText);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                location.reload();
                            } else
                            {
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                            }
                        });
                    });
                });
            </script>
        </div>

        <!------------------------------------------------->

    </body>
</html>

<%!
    
    private Retorno_MsgObj AgregarDatos(File fichero, Long PeriEstCod){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al guardar datos", TipoMensaje.ERROR));

        try
        {
            PeriodoEstudioDocumento periDocumento = new PeriodoEstudioDocumento();

        
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO

                if(PeriEstCod != null) periDocumento.setPeriodo(((PeriodoEstudio) LoPeriodo.GetInstancia().EstudioObtener(Long.valueOf(PeriEstCod)).getObjeto()));

                //if(DocCod != null) periDocumento = periDocumento.getPeriodo().getDocumentoById(Long.valueOf(DocCod));

                if(fichero != null) periDocumento.setArchivo(fichero);



          
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            periDocumento.setDocFch(new Date());

            retorno = (Retorno_MsgObj) LoPeriodo.GetInstancia().DocumentoAgregar(periDocumento);
        }
        catch(Exception ex)
        {
            retorno.setMensaje(new Mensajes("Error al guardar: " + ex.getMessage(), TipoMensaje.ERROR));
            ex.printStackTrace();
        }

        return retorno;
    }
    
   
%>
