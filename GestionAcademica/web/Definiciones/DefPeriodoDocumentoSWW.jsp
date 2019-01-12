<%-- 
    Document   : DefPeriodoEstudioWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Logica.LoParametro"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="Utiles.Mensajes"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Entidad.Persona"%>
<%@page import="Entidad.PeriodoEstudioDocumento"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.PeriodoEstudio"%>
<%@page import="java.util.List"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Utiles.Utilidades"%>
<%

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
                    fichero = new File(utilidad.getPrivateTempStorage(), item.getName());
                    item.write(fichero);
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
    }

    Modo Mode = Modo.valueOf(ModoTxt);

    String titulo = "";

    if (Mode.equals(Modo.INSERT)) {
        Retorno_MsgObj retorno = AgregarDatos(fichero, Long.valueOf(PeriEstCod));
        if (!retorno.SurgioError()) {
            response.sendRedirect(urlSistema + "Definiciones/DefPeriodoDocumentoSWW.jsp?MODO=" + Modo.UPDATE + "&pPeriEstCod=" + PeriEstCod);
        } else {
            out.println(retorno.getMensaje());
        }
    }

    List<PeriodoEstudioDocumento> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.EstudioObtener(Long.valueOf(PeriEstCod));
    if (!retorno.SurgioErrorObjetoRequerido()) {
        lstObjeto = ((PeriodoEstudio) retorno.getObjeto()).getLstDocumento();
        titulo = ((PeriodoEstudio) retorno.getObjeto()).getPeriodo().TextoPeriodo()
                + " - "
                +((PeriodoEstudio) retorno.getObjeto()).getCarreraCursoNombre() 
                + " - "
                + ((PeriodoEstudio) retorno.getObjeto()).getEstudioNombre();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String urlRetorno = urlSistema + "Definiciones/DefPeriodoEstudioSWW.jsp?MODO=" + Mode + "&pPeriCod=" + ((PeriodoEstudio) retorno.getObjeto()).getPeriodo().getPeriCod();
    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");


%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Periodo Estudio <%=titulo%> | Documentos</title>
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
                        <!-- TABS -->
                        <jsp:include page="/Definiciones/DefPeriodoEstudioTabs.jsp">
                            <jsp:param name="MostrarTabs" value="SI" />
                            <jsp:param name="Codigo" value="<%= PeriEstCod%>" />
                        </jsp:include>
                        <span class="contenedor_agregar">
                            <!--<a href="#" title="Ingresar" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#PopUpAgregar"> </a>-->
                           <%  
                               if(LoParametro.GetInstancia().obtener().getParUtlMdl())
                                {
                            %>
                                <a href="#" title="Sincronizar" class="glyphicon glyphicon-refresh" name="btn_sincronizar" id="btn_sincronizar"> </a>
                            <%
                                }
                            %>
                        </span>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">

                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <div name="datos_ocultos">
                                                            <input type="hidden" name="PeriEstCod" id="PeriEstCod" value="<% out.print(PeriEstCod); %>">
                                                        </div>
                                                        
                                                        <table id='tbl_ww' style=' <% out.print(tblVisible); %>' class='table table-hover'>
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
                                                                    <td><% out.print(utilidad.NuloToVacio(periDocumento.getObjFchMod())); %> </td>
                                                                    <td><% out.print("<a  target='_blank' href='" + urlSistema + "DescargarArchivo?pArcCod=" + periDocumento.getArcCod() + "' name='btn_descargar' id='btn_descargar' title='Descargar' class='glyphicon glyphicon-save btn_descargar'/>"); %> </td>
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
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>
                                                        
               
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
                    <form action="DefPeriodoDocumentoSWW.jsp" enctype="MULTIPART/FORM-DATA" method="post">
                        <div class="modal-body">

                            <div>
                                <input type="hidden" name="PeriEstCod" id="PeriEstCod" value="<% out.print(PeriEstCod); %>">
                                <input type="hidden" name="MODO" id="MODO" value="INSERT">
                                <input type="hidden" name="pPeriEstCod" id="pPeriEstCod" value="<% out.print(PeriEstCod); %>">
                                <input required type="file" name="file" /><br/>

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
                $(document).ready(function () {
                    
                    
                    $('#btn_sincronizar').on('click', function (e) {
                        var PeriEstCod = $('#PeriEstCod').val();
                        
                        $.post('<% out.print(urlSistema); %>AB_PeriodoEstudioDocumento', {
                            pPeriEstCod: PeriEstCod,
                            pAction: "SINCRONIZAR"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                location.reload();
                            }

                        });

        
                    });

                    $('.btn_eliminar').on('click', function (e) {

                        var codigo = $(this).data("codigo");
                        var nombre = $(this).data("nombre");

                        $('#elim_nombre').text(nombre);
                        $('#elim_boton_confirmar').data('codigo', codigo);


                    });

                    $('#elim_boton_confirmar').on('click', function (e) {
                        var PeriEstCod = $('#PeriEstCod').val();
                        var codigo = $('#elim_boton_confirmar').data('codigo');

                        $.post('<% out.print(urlSistema); %>AB_PeriodoEstudioDocumento', {
                            pPeriEstCod: PeriEstCod,
                            pDocCod: codigo,
                            pAction: "<% out.print(Modo.DELETE);%>"
                        }, function (responseText) {
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
