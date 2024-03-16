import { Need } from "./need";

export interface User {
    username: string;
    password: string;
    basket: Map<Need, number>;
    admin: boolean;
}