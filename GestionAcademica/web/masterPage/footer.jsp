<%-- 
    Document   : log_out
    Created on : 24-jun-2017, 11:58:10
    Author     : alvar
--%>


<div style="clear:both;"></div>

<div>
       <div id="div_pop_bkgr" name="div_pop_bkgr"></div>

       <div id="div_cargando" name="div_cargando">
           <div class="loading"></div>
       </div>

   </div>    

<div id="div_footer" class="panel-footer" style="text-align: right">
    <p>CTC Colonia - SGAPP</p>
</div>

<!-- PopUp para Ver Mensaje -->

<div id="PopUpMensaje"  class="modal fade" role="dialog">

    <!-- Modal -->
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h4 class="modal-title">Mensaje</h4>
          </div>
          <div class="modal-body">

              <label name="pop_asunto" id="pop_asunto"></label>
              <div name="pop_mensaje" id="pop_mensaje"></div>

          </div>
          <div class="modal-footer">
            <button name="ban_boton_confirmar" id="ban_boton_confirmar" type="button" class="btn btn-danger" data-codigo="">Eliminar</button>
            <button type="button" id="ban_boton_cancelar" name="ban_boton_cancelar" class="btn btn-default" >Cancelar</button>
          </div>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function() {
           var notVisible = false;
           var url     = $('#sga_url').val();
           
           $('#div_notificaciones').hide(); 
           
            $('#btn_ver_bandeja').on('click', function(e) {
                
                if(notVisible)
                {
                    $('#div_notificaciones').hide();
                    notVisible = false;
                }
                else
                {
                    $('#div_notificaciones').show();
                    notVisible = true;
                }

            });
            
            $(document).mouseup(function(e) 
            {
                var container = $("#div_notificaciones");

                // if the target of the click isn't the container nor a descendant of the container
                if (!container.is(e.target) && container.has(e.target).length === 0) 
                {
                    container.hide();
                }
            });
            
            $('#ban_boton_cancelar').on('click', function(e) {
                location.reload();
            });

            $('.btn_ver_msg').on('click', function(e) {
                var BanCod = $(this).data("codigo");
                $.post(url + 'NotificationManager', {
                                pNotBanCod: BanCod,
                                pAction: "MENSAJE_OBTENER"
                             }, function (responseText) {
                                 var obj = JSON.parse(responseText);

                                 if (obj.mensaje.tipoMensaje == 'ERROR')
                                 {
                                     MostrarMensaje(obj.mensaje.tipoMensaje, obj.mensaje.mensaje);
                                 }
                                 else
                                 {
                                    var bandeja = obj.objeto;
                                    //var bandeja = JSON.parse(objeto);
                                    var codigo = bandeja.notBanCod;
                                    var asunto = bandeja.notBanAsu;
                                    var mensaje = bandeja.notBanMen;

                                    $('#pop_asunto').text(asunto);
                                    $('#pop_mensaje').html(mensaje);
                                    $('#ban_boton_confirmar').data('codigo', codigo);

                                 }

                             });

              });

              $('#ban_boton_confirmar').on('click', function(e) {
                    var codigo  = $('#ban_boton_confirmar').data('codigo');
                    $.post(url + 'NotificationManager', {
                                pNotBanCod: codigo,
                                pAction: "ELIMINAR_MENSAJE"
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