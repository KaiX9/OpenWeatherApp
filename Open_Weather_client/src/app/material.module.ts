import { NgModule } from "@angular/core"

import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

const matModules: any[] = [
    MatToolbarModule,
    MatIconModule,
    MatButtonModule
]

@NgModule({
    imports: matModules,
    exports: matModules
})

export class MaterialModule { }