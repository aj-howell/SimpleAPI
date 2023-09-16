export default interface CustomerDTO
{
    name?:string,
    email?:string,
    gender?:string,
    age?:number,
    id?:number;
    roles:Array<string>
}