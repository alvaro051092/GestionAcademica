<%-- 
    Document   : DefPersona
    Created on : 30-jun-2017, 20:43:13
    Author     : alvar
--%>

<%@page import="Enumerado.Genero"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.Filial"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Entidad.Persona"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    LoPersona loPersona = LoPersona.GetInstancia();
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
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String PerCod = request.getParameter("pPerCod");
    String js_redirect = "location.replace('" + urlSistema + "Definiciones/DefPersonaWW.jsp');";

    Persona persona = new Persona();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        persona = (Persona) loPersona.obtener(Long.valueOf(PerCod)).getObjeto();
    }

    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";

    switch (Mode) {
        case INSERT:
            CamposActivos = "enabled";
            break;
        case DELETE:
            nameButton    = "ELIMINAR";
            nameClass     = "btn-danger";
            break;
        case UPDATE:
            CamposActivos = "enabled";
            nameButton    = "MODIFICAR";
            break;
    }

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Persona</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <script>
            $(document).ready(function () {
                
                $('#btn_guardar').click(function (event) {
                    if($(this).data("accion") == "<%=Mode.DELETE%>")
                    {
                        $(function () {
                            $('#PopUpConfEliminar').modal('show');
                        });
                    }
                    else
                    {
                        if(validarDatos())
                        {
                            procesarDatos();
                        }
                    }
                });
                
                function validarDatos(){
                    
                    if(!$('#frm_general')[0].checkValidity())
                    {
                        var $myForm = $('#frm_general');
                        $myForm.find(':submit').click();
                        return false;
                    }

                    return true;
                }
                
                

            });


function procesarDatos(){
                    var PerCod= $('#PerCod').val();
                    var PerApe= $('#PerApe').val();
                    var PerDoc= $('#PerDoc').val();
                    var PerEml= $('#PerEml').val();
                    var PerEsAdm= $('#PerEsAdm').is(':checked');
                    var PerEsAlu= $('#PerEsAlu').is(':checked');
                    var PerEsDoc= $('#PerEsDoc').is(':checked');
                    var PerFil= $('select[name=PerFil]').val();
                    var PerNom= $('#PerNom').val();
                    var PerNotApp= $('#PerNotApp').is(':checked');
                    var PerNotEml= $('#PerNotEml').is(':checked');
                    var PerNroEstOrt= $('#PerNroEstOrt').val();
                    var PerNroLib= $('#PerNroLib').val();
                    var PerPass= $('#PerPass').val();
                    var PerUsrMod= $('#PerUsrMod').val();
                    var PerApe2= $('#PerApe2').val();
                    var PerBeca= $('#PerBeca').val();
                    var PerCiudad= $('#PerCiudad').val();
                    var PerDir= $('#PerDir').val();
                    var PerDto= $('#PerDto').val();
                    var PerFchNac= $('#PerFchNac').val();
                    var PerGen= $('select[name=PerGen]').val();
                    var PerObs= $('#PerObs').val();
                    var PerPais= $('#PerPais').val();
                    var PerProf= $('#PerProf').val();
                    var PerSecApr= $('#PerSecApr').val();
                    var PerTel= $('#PerTel').val();
                    var PerTpoBeca= $('#PerTpoBeca').val();

                    var modo = $('#MODO').val();

                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<%=urlSistema%>ABM_Persona', {
                            pPerCod: PerCod,
                            pPerApe: PerApe,
                            pPerDoc: PerDoc,
                            pPerEml: PerEml,
                            pPerEsAdm: PerEsAdm,
                            pPerEsAlu: PerEsAlu,
                            pPerEsDoc: PerEsDoc,
                            pPerFil: PerFil,
                            pPerNom: PerNom,
                            pPerNotApp: PerNotApp,
                            pPerNotEml: PerNotEml,
                            pPerNroEstOrt: PerNroEstOrt,
                            pPerNroLib: PerNroLib,
                            pPerPass: PerPass,
                            pPerUsrMod: PerUsrMod,
                            pPerApe2: PerApe2,
                            pPerBeca: PerBeca,
                            pPerCiudad: PerCiudad,
                            pPerDir: PerDir,
                            pPerDto: PerDto,
                            pPerFchNac: PerFchNac,
                            pPerGen: PerGen,
                            pPerObs: PerObs,
                            pPerPais: PerPais,
                            pPerProf: PerProf,
                            pPerSecApr: PerSecApr,
                            pPerTel: PerTel,
                            pPerTpoBeca: PerTpoBeca,
                            //pArcCod: ArcCod,
                            pAction: modo
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                <%=js_redirect%>
                            } 

                        });
                    
                }

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
                            <jsp:include page="/Definiciones/DefPersonaTabs.jsp"/>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">

                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            
                                                            <!-- DATOS PERSONALES -->
                                                            <div name="campos_ocultos">
                                                                <input type="hidden" name="MODO" id="MODO" value="<%=Mode%>">                                                                
                                                            </div>
                                                            
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2 class="">Datos Personales</h2>
                                                            </div>

                                                            <div class="form-group "><label for="PerCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="PerCod" name="PerCod" disabled value="<%=utilidad.NuloToVacio(persona.getPerCod())%>" ></div></div>
                                                            <div class="form-group "><label for="PerNom" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="PerNom" name="PerNom" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerNom())%>" ></div></div>
                                                            <div class="form-group "><label for="PerApe" class="control-label col-lg-3">Primer apellido</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="PerApe" name="PerApe" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerApe())%>" ></div></div>
                                                            <div class="form-group "><label for="PerApe2" class="control-label col-lg-3">Segundo apellido</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerApe2" name="PerApe2" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerApe2())%>" ></div></div>
                                                            <div class="form-group "><label for="PerDoc" class="control-label col-lg-3">Documento</label><div class="col-lg-6"><input type="text" required class=" form-control inputs_generales" id="PerDoc" name="PerDoc" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerDoc())%>" ></div></div>
                                                            <div class="form-group "><label for="PerFchNac" class="control-label col-lg-3">Fecha de nacimiento</label><div class="col-lg-6"><input type="date" required class=" form-control inputs_generales" id="PerFchNac" name="PerFchNac" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerFchNac())%>" ></div></div>
                                                            
                                                            <div class="form-group ">
                                                                <label for="PerGen" class="control-label col-lg-3">Género</label>
                                                                <div class="col-lg-6">
                                                                    <select class=" form-control inputs_generales" id="PerGen" name="PerGen" <%=CamposActivos%> >
                                                                    <%
                                                                        for (Genero genero : Genero.values()) {
                                                                            out.println("<option " + (genero == persona.getPerGen() ? "selected" : "") + " value='" + genero.name() + "'>" + genero.getValor() + "</option>");
                                                                        }
                                                                    %>
                                                                    </select>
                                                                </div>
                                                            </div>      
                                                            
                                                            <div class="form-group "><label for="PerProf" class="control-label col-lg-3">Profesión</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerProf" name="PerProf" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerProf())%>" ></div></div>
                                                            <div class="form-group "><label for="PerSecApr" class="control-label col-lg-3">Secundaria aprobada</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerSecApr" name="PerSecApr" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerSecApr())%>" ></div></div>
                                                            <div class="form-group "><label for="PerTel" class="control-label col-lg-3">Teléfono</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerTel" name="PerTel" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerTel())%>" ></div></div>
                                                            
                                                            <div class="formulario_borde"></div>
                                                            
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Dirección</h2>
                                                            </div>
                                                            
                                                            <!-- DIRECCION -->
                                                            <div class="form-group "><label for="PerDir" class="control-label col-lg-3">Dirección</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerDir" name="PerDir" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerDir())%>" ></div></div>
                                                            <div class="form-group "><label for="PerCiudad" class="control-label col-lg-3">Ciudad</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerCiudad" name="PerCiudad" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerCiudad())%>" ></div></div>
                                                            <div class="form-group "><label for="PerDto" class="control-label col-lg-3">Departamento</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerDto" name="PerDto" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerDto())%>" ></div></div>
                                                            <div class="form-group "><label for="PerPais" class="control-label col-lg-3">País</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerPais" name="PerPais" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerPais())%>" ></div></div>

                                                            <div class="formulario_borde"></div>
                                                            
                                                            <!-- TIPO DE USUARIO -->
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Tipo de Usuario</h2>
                                                            </div>                                                            
                                                            
                                                            <div class="form-group "><label for="PerEsAdm" class="control-label col-lg-3">Administrador</label><div class="col-lg-6"><input type="checkbox" class=" inputs_generales" id="PerEsAdm" name="PerEsAdm" <%=CamposActivos%> <%=utilidad.BooleanToChecked(persona.getPerEsAdm())%> ></div></div>
                                                            <div class="form-group "><label for="PerEsAlu" class="control-label col-lg-3">Alumno</label><div class="col-lg-6"><input type="checkbox" class=" inputs_generales" id="PerEsAlu" name="PerEsAlu" <%=CamposActivos%> <%=utilidad.BooleanToChecked(persona.getPerEsAlu())%> ></div></div>
                                                            <div class="form-group "><label for="PerEsDoc" class="control-label col-lg-3">Docente</label><div class="col-lg-6"><input type="checkbox" class=" inputs_generales" id="PerEsDoc" name="PerEsDoc" <%=CamposActivos%> <%=utilidad.BooleanToChecked(persona.getPerEsDoc())%> ></div></div>

                                                            <div class="formulario_borde"></div>
                                                            
                                                            <!-- NOTIFICACION -->
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Notificación</h2>
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="PerNotApp" class="control-label col-lg-3">Aplicación</label><div class="col-lg-6"><input type="checkbox" class=" inputs_generales" id="PerNotApp" name="PerNotApp" <%=CamposActivos%> <%=utilidad.BooleanToChecked(persona.getPerNotApp())%> ></div></div>
                                                            <div class="form-group "><label for="PerNotEml" class="control-label col-lg-3">Email</label><div class="col-lg-6"><input type="checkbox" class=" inputs_generales" id="PerNotEml" name="PerNotEml" <%=CamposActivos%> <%=utilidad.BooleanToChecked(persona.getPerNotEml())%> ></div></div>
                                                            
                                                            <div class="formulario_borde"></div>
                                                            
                                                            <!-- NOTIFICACION -->
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Estudiante</h2>
                                                            </div>

                                                            <div class="form-group ">
                                                                <label for="PerFil" class="control-label col-lg-3">Filial</label>
                                                                <div class="col-lg-6">
                                                                    <select class=" form-control inputs_generales" id="PerFil" name="PerFil" <%=CamposActivos%>>
                                                                    <%
                                                                        for (Filial filial : Filial.values()) {
                                                                            out.println("<option " + (filial == persona.getPerFil() ? "selected" : "") + " value='" + filial.getFilial() + "'>" + filial.getFilialNom() + "</option>");
                                                                        }
                                                                    %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group "><label for="PerNroEstOrt" class="control-label col-lg-3">Número de estudiante (ORT)</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="PerNroEstOrt" name="PerNroEstOrt" <%=CamposActivos%> value="<%=utilidad.NuloToCero(persona.getPerNroEstOrt())%>" ></div></div>
                                                            <div class="form-group "><label for="PerNroLib" class="control-label col-lg-3">Número de libra</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="PerNroLib" name="PerNroLib" <%=CamposActivos%> value="<%=utilidad.NuloToCero(persona.getPerNroLib())%>" ></div></div>
                                                            <div class="form-group "><label for="PerBeca" class="control-label col-lg-3">Porcentaje de beca</label><div class="col-lg-6"><input type="number" step="0.01" max="100" min="0" class=" form-control inputs_generales" id="PerBeca" name="PerBeca" <%=CamposActivos%> value="<%=utilidad.NuloToCero(persona.getPerBeca())%>" ></div></div>
                                                            <div class="form-group "><label for="PerTpoBeca" class="control-label col-lg-3">Tipo de beca</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerTpoBeca" name="PerTpoBeca" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerTpoBeca())%>" ></div></div>

                                                            <div class="formulario_borde"></div>
                                                            
                                                            <!-- DATOS DE USUARIO -->
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Datos de Usuario</h2>
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="PerEml" class="control-label col-lg-3">Email</label><div class="col-lg-6"><input type="email" required class=" form-control inputs_generales" id="PerEml" name="PerEml" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerEml())%>" ></div></div>
                                                            <div class="form-group "><label for="PerUsrMod" class="control-label col-lg-3">Usuario</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PerUsrMod" name="PerUsrMod" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerUsrMod())%>" ></div></div>
                                                            <div class="form-group "><label for="PerPass" class="control-label col-lg-3">Contraseña</label><div class="col-lg-6"><input type="password" class=" form-control inputs_generales" id="PerPass" name="PerPass" <%=CamposActivos%> value="" ></div></div>
                                                            
                                                            <div class="formulario_borde"></div>
                                                            
                                                            <!-- OBSERVACIONES -->
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Observaciones</h2>
                                                            </div>                                                            
                                                            
                                                            <div class="form-group "><label for="PerObs" class="control-label col-lg-3">Observaciones</label><div class="col-lg-6"><textarea rows="10" class=" form-control inputs_generales" id="PerObs" name="PerObs" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(persona.getPerObs())%>" ></textarea></div></div>

                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input type="button" class="btn <%=nameClass%>" data-accion="<%=Mode%>" name="btn_guardar" id="btn_guardar" value="<%=nameButton%>">
                                                                    <input type="button" class="btn btn-default" onclick="<%=js_redirect%>" value="CANCELAR">
                                                                </div>
                                                            </div>
                                                        </form>

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
        
        <!--Popup Confirmar Eliminación-->
        <div id="PopUpConfEliminar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Eliminar</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <h4>Confirma eliminación?</h4>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button name="btn_conf_eliminar" id="btn_conf_eliminar" class="btn btn-danger" data-dismiss="modal" onclick="procesarDatos()">Eliminar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div>  

    </body>
</html>
