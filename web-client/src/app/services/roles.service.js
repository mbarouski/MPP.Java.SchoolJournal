"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var core_1 = require("@angular/core");
var rxjs_1 = require("rxjs");
var http_1 = require("@angular/http");
var app_config_1 = require("../configs/app.config");
var RolesService = (function () {
    function RolesService(config, http) {
        this.config = config;
        this.http = http;
        this.rolesSubject = new rxjs_1.ReplaySubject(1);
        this.updateRoles();
    }
    RolesService.prototype.updateRoles = function () {
        var _this = this;
        return this.http.get(this.config.apiEndpoint + "/roles")
            .map(function (res) {
            return res.json();
        })
            .subscribe(function (roles) {
            debugger;
            _this.rolesSubject.next(roles);
        });
    };
    RolesService = __decorate([
        core_1.Injectable(),
        __param(0, core_1.Inject(app_config_1.APP_CONFIG)), 
        __metadata('design:paramtypes', [Object, http_1.Http])
    ], RolesService);
    return RolesService;
}());
exports.RolesService = RolesService;
//# sourceMappingURL=roles.service.js.map