import { Routes } from '@angular/router';

import { LoginComponent } from './components/login/login';
import { MenuComponent } from './menu/menu.component';
import { ReservarComponent } from './reservar/reservar.component';
import { ClasesComponent } from './clases/clases.component';
import { ReservarClaseComponent } from './reservar-clase/reservar-clase.component';
import { Administrador} from './administrador/administrador';
import { RegistrarClienteComponent } from './registrar-cliente/registrar-cliente';
import { ActualizarCliente } from './actualizar-cliente/actualizar-cliente';
import { GestionClasesComponent } from './gestion-clases/gestion-clases.component';
import { roleGuard } from './services/role.guard';
import { MisReservasComponent } from './mis-reservas/mis-reservas.component';
import { SupervisarAforoComponent } from './supervisar-aforo/supervisar-aforo.component';
import { GestionProductosComponent } from './gestion-productos/gestion-productos.component';
import { ComprarProductosComponent } from './comprar-productos/comprar-productos.component';
import { InstructorPanelComponent } from './instructor/instructor-panel.component';
import { InstructorClasesComponent } from './instructor/instructor-clases.component';
import { InstructorClaseDetalleComponent } from './instructor/instructor-clase-detalle.component';
import { InstructorPerfilComponent } from './instructor/instructor-perfil.component';
import { PagoComponent } from './pago/pago.component';
import { MisComprasComponent } from './mis-compras/mis-compras.component';

export const routes: Routes = [
  {
    path: 'login',
    title: 'Iniciar sesión | Punto Marcial',
    component: LoginComponent
  },
  {
    path: 'menu',
    title: 'Menú del cliente | Punto Marcial',
    component: MenuComponent,
    canActivate: [roleGuard('CLIENTE')]
  },

  {
  path: 'registrar-usuario',
  title: 'Registrar usuario | Punto Marcial',
  component: RegistrarClienteComponent,
  canActivate: [roleGuard('ADMINISTRADOR')]
  },

  {
  path: 'actualizar-usuario',
  title: 'Actualizar usuario | Punto Marcial',
  component: ActualizarCliente,
  canActivate: [roleGuard('ADMINISTRADOR')]
  },

  {
    path: 'reservar',
    title: 'Reservar | Punto Marcial',
    component: ReservarComponent,
    canActivate: [roleGuard('CLIENTE')]
  },
  {
    path: 'clases',
    title: 'Clases disponibles | Punto Marcial',
    component: ClasesComponent,
    canActivate: [roleGuard('CLIENTE')]
  },
  {
    path: 'reservar-clase',
    title: 'Confirmar reserva | Punto Marcial',
    component: ReservarClaseComponent,
    canActivate: [roleGuard('CLIENTE')]
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
  path: 'gestion-clases',
  title: 'Gestionar clases | Punto Marcial',
  component: GestionClasesComponent,
  canActivate: [roleGuard('ADMINISTRADOR')]
  },
  {
  path: 'administrador',
  title: 'Panel de administración | Punto Marcial',
  component: Administrador,
  canActivate: [roleGuard('ADMINISTRADOR')]
  },{
  path: 'mis-reservas',
  title: 'Mis reservas | Punto Marcial',
  component: MisReservasComponent,
  canActivate: [roleGuard('CLIENTE')]
},
{
  path: 'supervisar-aforo',
  title: 'Supervisar aforo | Punto Marcial',
  component: SupervisarAforoComponent,
  canActivate: [roleGuard('ADMINISTRADOR')]
},
{
  path: 'gestion-productos',
  title: 'Gestionar productos | Punto Marcial',
  component: GestionProductosComponent,
  canActivate: [roleGuard('ADMINISTRADOR')]
},
{
  path: 'comprar-productos',
  title: 'Comprar productos | Punto Marcial',
  component: ComprarProductosComponent,
  canActivate: [roleGuard('CLIENTE')]
},
{
  path: 'pago',
  title: 'Pago | Punto Marcial',
  component: PagoComponent,
  canActivate: [roleGuard('CLIENTE')]
},
{
  path: 'mis-compras',
  title: 'Mis compras | Punto Marcial',
  component: MisComprasComponent,
  canActivate: [roleGuard('CLIENTE')]
},
// ---- Área del instructor (solo ve sus clases asignadas) ----
{
  path: 'instructor',
  title: 'Panel del instructor | Punto Marcial',
  component: InstructorPanelComponent,
  canActivate: [roleGuard('INSTRUCTOR')]
},
{
  path: 'instructor/clases',
  title: 'Mis clases | Punto Marcial',
  component: InstructorClasesComponent,
  data: { pasadas: false },
  canActivate: [roleGuard('INSTRUCTOR')]
},
{
  path: 'instructor/historial',
  title: 'Historial de clases | Punto Marcial',
  component: InstructorClasesComponent,
  data: { pasadas: true },
  canActivate: [roleGuard('INSTRUCTOR')]
},
{
  path: 'instructor/clases/:id',
  title: 'Alumnos y asistencia | Punto Marcial',
  component: InstructorClaseDetalleComponent,
  canActivate: [roleGuard('INSTRUCTOR')]
},
{
  path: 'instructor/perfil',
  title: 'Mi perfil | Punto Marcial',
  component: InstructorPerfilComponent,
  canActivate: [roleGuard('INSTRUCTOR')]
}
];
